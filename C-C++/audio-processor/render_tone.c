#include <stdio.h>
#include <stdlib.h>
#include <stdint.h>
#include <string.h>
#include <math.h>
#include <assert.h>
#include "io.h"
#include "wave.h"

/**                                                                                    
* Generate wave accordingly.                                                           
* @param mono_buf the array we operate on.                                             
* @param waveform the wave num specified by user.                                       
* @param frequency the frequency for needed wave.  
* @param numsamplesm the amount of values.
* @param amplitude the gain we are applying.     
*/
void generate_wave(int16_t *mono_buf,int waveform, float frequency, int numsamples, float amplitude){
  if (waveform==0) {
    generate_sine_wave(mono_buf, numsamples, frequency);
    apply_gain(mono_buf, numsamples, amplitude);
  } else if(waveform==1) {
    generate_square_wave(mono_buf, numsamples, frequency);
    apply_gain(mono_buf, numsamples, amplitude);
  } else if(waveform==2) {
    generate_saw_wave(mono_buf, numsamples, frequency);
    apply_gain(mono_buf, numsamples, amplitude);
  } else {
    fatal_error("Invalid waveform input");
  }
}
  
int main(int argc, const char *argv[]){
  if (argc !=6){
    fatal_error("argument missing");
    return 2;
  }
  int waveform = -1, numsamples = 0;
  float frequency = 0, amplitude = 0;
  // Read in waveform, frequency, amplitude, numsamples and test validity
  if (sscanf(argv[1], "%d", &waveform)!= 1) fatal_error("Invalid waveform");
  if (sscanf(argv[2], "%f", &frequency)!= 1) fatal_error("Invalid frequency");
  if (sscanf(argv[3], "%f", &amplitude)!= 1) fatal_error("Invalid amplitude");
  if (sscanf(argv[4], "%d", &numsamples)!=1) fatal_error("Invalid num_samples");
  const char *wavfileout = argv[5];
  int16_t *mono_buf = (int16_t*) malloc(2*sizeof(int16_t) * numsamples);
  generate_wave(mono_buf,waveform,frequency,numsamples,amplitude);
  //mix and output to stereo_buf
  int16_t *stereo_buf = (int16_t*) malloc(sizeof(int16_t) * 2 * numsamples);
  for (int i = 0; i<2 * numsamples; i++){
     stereo_buf[0]=0;
  }
  mix_in(stereo_buf, 0, mono_buf, numsamples);
  mix_in(stereo_buf, 1, mono_buf, numsamples);
  FILE* output = fopen(wavfileout, "wb");
  write_wave_header(output, numsamples);
  write_s16_buf(output, stereo_buf, numsamples*2);
  fclose(output);
  free(mono_buf);
  free(stereo_buf);
  return 0;
}

