#include <stdio.h>
#include <stdlib.h>
#include <stdint.h>
#include <string.h>
#include <math.h>
#include <assert.h>
#include "io.h"
#include "wave.h"

void write_wave_header(FILE *out, unsigned num_samples) {
  //
  // See: http://soundfile.sapp.org/doc/WaveFormat/
  //

  uint32_t ChunkSize, Subchunk1Size, Subchunk2Size;
  uint16_t NumChannels = NUM_CHANNELS;
  uint32_t ByteRate = SAMPLES_PER_SECOND * NumChannels * (BITS_PER_SAMPLE/8u);
  uint16_t BlockAlign = NumChannels * (BITS_PER_SAMPLE/8u);

  // Subchunk2Size is the total amount of sample data
  Subchunk2Size = num_samples * NumChannels * (BITS_PER_SAMPLE/8u);
  Subchunk1Size = 16u;
  ChunkSize = 4u + (8u + Subchunk1Size) + (8u + Subchunk2Size);

  // Write the RIFF chunk descriptor
  write_bytes(out, "RIFF", 4u);
  write_u32(out, ChunkSize);
  write_bytes(out, "WAVE", 4u);

  // Write the "fmt " sub-chunk
  write_bytes(out, "fmt ", 4u);       // Subchunk1ID
  write_u32(out, Subchunk1Size);
  write_u16(out, 1u);                 // PCM format
  write_u16(out, NumChannels);
  write_u32(out, SAMPLES_PER_SECOND); // SampleRate
  write_u32(out, ByteRate);
  write_u16(out, BlockAlign);
  write_u16(out, BITS_PER_SAMPLE);

  // Write the beginning of the "data" sub-chunk, but not the actual data
  write_bytes(out, "data", 4);        // Subchunk2ID
  write_u32(out, Subchunk2Size);
}

void read_wave_header(FILE *in, unsigned *num_samples) {
  char label_buf[4];
  uint32_t ChunkSize, Subchunk1Size, SampleRate, ByteRate, Subchunk2Size;
  uint16_t AudioFormat, NumChannels, BlockAlign, BitsPerSample;

  read_bytes(in, label_buf, 4u);
  if (memcmp(label_buf, "RIFF", 4u) != 0) {
    fatal_error("Bad wave header (no RIFF label)");
  }

  read_u32(in, &ChunkSize); // ignore

  read_bytes(in, label_buf, 4u);
  if (memcmp(label_buf, "WAVE", 4u) != 0) {
    fatal_error("Bad wave header (no WAVE label)");
  }

  read_bytes(in, label_buf, 4u);
  if (memcmp(label_buf, "fmt ", 4u) != 0) {
    fatal_error("Bad wave header (no 'fmt ' subchunk ID)");
  }

  read_u32(in, &Subchunk1Size);
  //printf("Subchunk1Size=%u\n", Subchunk1Size);
  if (Subchunk1Size != 16u) {
    fatal_error("Bad wave header (Subchunk1Size was not 16)");
  }

  read_u16(in, &AudioFormat);
  if (AudioFormat != 1u) {
    fatal_error("Bad wave header (AudioFormat is not PCM)");
  }

  read_u16(in, &NumChannels);
  if (NumChannels != NUM_CHANNELS) {
    fatal_error("Bad wave header (NumChannels is not 2)");
  }

  read_u32(in, &SampleRate);
  if (SampleRate != SAMPLES_PER_SECOND) {
    fatal_error("Bad wave header (Unexpected sample rate)");
  }

  read_u32(in, &ByteRate); // ignore

  read_u16(in, &BlockAlign); // ignore

  read_u16(in, &BitsPerSample);
  if (BitsPerSample != BITS_PER_SAMPLE) {
    fatal_error("Bad wave header (Unexpected bits per sample)");
  }

  read_bytes(in, label_buf, 4u);
  if (memcmp(label_buf, "data", 4u) != 0) {
    fatal_error("Bad wave header (no 'data' subchunk ID)");
  }

  // finally we're at the Subchunk2Size field, from which we can
  // determine the number of samples
  read_u32(in, &Subchunk2Size);
  *num_samples = Subchunk2Size / NUM_CHANNELS / (BITS_PER_SAMPLE/8u);
}

void compute_pan(float angle, float channel_gain[]) {
  channel_gain[0] = (sqrt(2) /(float) 2) * (cos(angle) + sin(angle));
  channel_gain[1] = (sqrt(2) /(float) 2) * (cos(angle) - sin(angle));
}

void generate_sine_wave(int16_t mono_buf[], unsigned num_samples, float freq_hz) {
  double period = 1.0 / SAMPLES_PER_SECOND;
  for (int i = 0; i < (int)num_samples; i++) {
    int amp = 32768 * sin((i * period) * freq_hz * 2 * PI);
    if (amp > 32767) {
      amp = 32767;
    }
    mono_buf[i]=amp;
  }
}

void generate_square_wave(int16_t mono_buf[], unsigned num_samples, float freq_hz) {
  double period = 1.0 / SAMPLES_PER_SECOND;
  double half_cycle = (0.5) / freq_hz;
  int half = 0;//record how many half cycle have been through
  for (int i = 0; i < (int)num_samples; i++) {
    if ((i * period - half * half_cycle) > half_cycle) {
      half++;
    }
    if (half%2 == 0) {
      mono_buf[i] = 32767;
    } else {
      mono_buf[i] = -32768;
    }
  }
}

void generate_saw_wave(int16_t mono_buf[], unsigned num_samples, float freq_hz) {
  double time_intervel = 1.0 / SAMPLES_PER_SECOND;
  double cycle = 1.0/freq_hz;
  for (int i=0;i<(int)num_samples;i++) {
    int round = (i*time_intervel)/cycle;
    double t = (i*time_intervel)-cycle*round;//get its place in a full cycle
    mono_buf[i]= -32768+t/cycle*(32768+32767);
  }
}

void apply_gain(int16_t mono_buf[], unsigned num_samples, float gain) {
  for (int i = 0; i < (int)num_samples; i++) {
    mono_buf[i] *= gain;
  }
}

void apply_adsr_envelope(int16_t mono_buf[], unsigned num_samples) {
  //corner case: less than full attack
  if(num_samples<(ATTACK_NUM_SAMPLES+DECAY_NUM_SAMPLES+RELEASE_NUM_SAMPLES)){
    for (int i = 0; i < (int)(num_samples/2); i++) {
      mono_buf[i] *= (1.0/(num_samples/2)) * i;
    }
    for (int i = (int)num_samples/2; i < (int)num_samples; i++) {
      mono_buf[i] *= (-1.0/num_samples+1.0) * i;
    }
    return;
  } 
  for (int i = 0; i < ATTACK_NUM_SAMPLES; i++) {//allocate based on phases
    mono_buf[i] *= (1.2/ATTACK_NUM_SAMPLES) * i;
  }
  for (int j = ATTACK_NUM_SAMPLES; j < ATTACK_NUM_SAMPLES + DECAY_NUM_SAMPLES; j++) {
    mono_buf[j] *= (ATTACK_NUM_SAMPLES+DECAY_NUM_SAMPLES-j)*(0.2/DECAY_NUM_SAMPLES) + 1;
  }
  for (int k = (float)num_samples - RELEASE_NUM_SAMPLES; k < (float)num_samples; k++) {
    mono_buf[k] *= (num_samples - k)*(1.0/RELEASE_NUM_SAMPLES);
  }
}

void mix_in(int16_t stereo_buf[], unsigned channel, const int16_t mono_buf[], unsigned num_samples) {
  int start;
  if (channel==0){//insert position is different based on channel
    start=0;
  } else{
    start=1;
  }
  for (int i = 0; i < (int)num_samples; i++){//ensure within bound
    int32_t add= mono_buf[i]+stereo_buf[start+ 2 * i];
    if(add>32767){
      add=32767;
    } else if(add<-32768){
      add=-32768;
    }
    stereo_buf[start + 2 * i]=add;
  }
}
