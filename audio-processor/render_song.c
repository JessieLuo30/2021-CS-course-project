#include <stdio.h>
#include <stdlib.h>
#include <stdint.h>
#include <string.h>
#include <math.h>
#include "io.h"
#include "wave.h"

struct Instrument {
  unsigned int waveform;
  float angle;
  unsigned int enable;
  float gain;
};

/**
 * Initializes the struct Instrument.
 * @param instruments the struct array.
 * @param number the index of the target instrument.
 */
void defaultIns(struct Instrument *instruments, int number) {
  for (int i = 0; i < number; i++){
    instruments[i].waveform = 0;
    instruments[i].angle = 0.0;
    instruments[i].enable = 0;
    instruments[i].gain = 0.2;
  }
}

/**                                                             
 * Generate wave accordingly.
 * @param mono_buf the array we operate on.                                        
 * @param instruments the struct array.
 * @param size the amount of values.                      
 * @param frequency the frequency for needed wave.                   
 */
void wave_setup(int16_t *mono_buf, struct Instrument *instrument, int size, float frequency) {
  int option = instrument->waveform;
  if (option == 0) {
    generate_sine_wave(mono_buf, size, frequency);
  } else if (option == 1) {
    generate_square_wave(mono_buf, size, frequency);
  } else if (option == 2) {
    generate_saw_wave(mono_buf, size, frequency);
  } else {
    fatal_error ("invalid/missing data for a directive");
  }
}

/**
 * Apply channel gain to right and left channel and mix them to stereo_buf.
 * @param mono_buf before apply channel gain.
 * @param stereo_buf the output buffer.
 * @param mono_left left channel.
 * @param mono_right right channel. 
 * @param channel_gain the computed pan. 
 * @param instruments the instrument struct.
 * @param size the amount of values. 
 * @param start the start index of mixing.
 */
void channel_setup(int16_t *mono_buf, int16_t *stereo_buf, int16_t *mono_left, int16_t *mono_right, float *channel_gain, struct Instrument instrument, int size, int start) {
  float angle = instrument.angle;
  compute_pan(angle, channel_gain);
  for (int i = 0; i < size; i++) {
    mono_right[i] = mono_buf[i];
    mono_left[i] = mono_buf[i];
  }
  apply_gain(mono_right, size, channel_gain[1]);
  apply_gain(mono_left, size, channel_gain[0]);
  mix_in(stereo_buf + (start * 2), 0, mono_left, size);
  mix_in(stereo_buf + (start * 2), 1, mono_right, size);
  free(mono_buf);
  free(mono_right);
  free(mono_left);
}

/**                                                                                    
 * Apply channel gain to right and left channel and mix them to stereo_buf.             
 * @param input pointer to the input file.                                           
 * @param instruments struct instruments array.            
 * @param ins index of instrument.                                                       
 * @param mono_right right channel.                
 * @param mono_left left channel.
 * @param mono_buf before apply channel gain.
 * @param stereo_buf the output buffer.                                
 * @param channel_gain the computed pan.                                                
 * @param count how many values read in successfully.                                    
 */
void case_N(FILE *input, struct Instrument instruments[], int ins, int16_t  *mono_right,int16_t  *mono_left,int16_t  *mono_buf, int16_t *stereo_buf, float channel_gain[], int count){
  int start, end, note, size, frequency;
  float gain;
  count = fscanf(input, " %d %d %d %f ", &start, &end, &note, &gain);
  size = end - start;
  frequency = 440.0 * pow(2.0,(double)(note-69.0)/12.0);
  if (count != 4){
    fatal_error ("invalid/missing data for a directive");
  }
  mono_buf = malloc(sizeof(int16_t) * (size));
  wave_setup(mono_buf, &instruments[ins], size, frequency);
  apply_gain(mono_buf, size, instruments[ins].gain);
  apply_gain(mono_buf, size, gain);
  if (instruments[ins].enable ==1) apply_adsr_envelope(mono_buf, size);
  mono_left = malloc(sizeof(int16_t) * size);
  mono_right = malloc(sizeof(int16_t) * size);
  channel_setup(mono_buf, stereo_buf, mono_left, mono_right, channel_gain, instruments[ins], size, start);
}

/**                                                                                                                  
 * Perform different actions based on the directive input.                                          
 * @param input pointer to the input file.
 * @param direct directives that determine which operations will be performed.
 * @param instruments struct instruments array. 
 * @param ins index of instrument.
 * @param stereo_buf the output buffer.     
 */   
void case_switch(FILE *input, char direct, struct Instrument instruments[], int ins, int16_t *stereo_buf){
  int waveform, enable, count = 0;
  float gain, angle;
  float channel_gain[2];
  int16_t *mono_right = 0, *mono_left = 0, *mono_buf = 0;
  switch(direct){
  case 'N':
    case_N(input, instruments, ins, mono_right, mono_left, mono_buf, stereo_buf, channel_gain, count);
    break;
  case 'W':
    count = fscanf(input, " %d ", &waveform);
    if (count == 1)instruments[ins].waveform=waveform;
    else fatal_error ("invalid/missing data for a directive" );
    break;
  case 'E' :
    count = fscanf(input, " %d ", &enable);
    if (count == 1)instruments[ins].enable=enable;
    else fatal_error ("invalid/missing data for a directive" );
    break;
  case 'P' :
    count = fscanf(input, " %f ", &angle);
    if (count == 1)instruments[ins].angle=angle;
    else fatal_error ("invalid/missing data for a directive" );
    break;
  case 'G' :
    count = fscanf(input, " %f ", &gain);
    if (count == 1) instruments[ins].gain=gain;
    else fatal_error ("invalid/missing data for a directive" );
    if (gain > 1 || gain < 0) fatal_error ("gain out of range");
    break;
  default :
    fatal_error("invalid directive character");
  }
}

int main(int argc, char* argv[]){
  struct Instrument instruments[16];
  defaultIns(instruments, 2);
  //get input values are test their validity
  if (argc !=3){
    fatal_error("argument missing");
    return 2;
  }
  FILE *input = fopen(argv[1],"rb");
  if (input == NULL) {
    fatal_error("could not open input file");
    return 1;
  }
  int sample;
  int16_t *stereo_buf;
  int first = fscanf(input, " %d ", &sample);
  if (first == 1) stereo_buf = malloc(sizeof(int16_t)*sample*2);
  else fatal_error ("invalid/missing data for sample num" );
  for (int i = 0; i < sample * 2; i++) stereo_buf[i] = 0;
  char direct;
  int ins;
  //scan line by line, took in the first two values first every time
  int numCollected = fscanf(input, " %c %d ", &direct, &ins);
  while (numCollected == 2){
    case_switch(input,direct, instruments, ins, stereo_buf);
    numCollected = fscanf(input, " %c %d " , &direct, &ins);
  }
  if (ferror(input)) {
    fatal_error ("error indicator" );
  } else if (numCollected != EOF) {
    fatal_error ("invalid/missing data for a directive" );
  }
  //output to stereo_buf file
  FILE* output = fopen(argv[2], "wb");
  write_wave_header(output, sample);
  write_s16_buf(output, stereo_buf, sample*2);
  fclose(output);
  fclose(input);
  free(stereo_buf);
  return 0;
}
