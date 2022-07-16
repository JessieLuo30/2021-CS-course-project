
#include <stdio.h>
#include <stdlib.h>
#include <stdint.h>
#include <string.h>
#include <assert.h>
#include "io.h"
#include "wave.h"

int main(int argc, const char *argv[]){
  //read and test the validity of arguments
  if (argc !=5){
    fatal_error("argument missing");
    return 2;
  }
  const char *before = argv[1];
  const char *after= argv[2];
  int delay = 0;
  if (sscanf(argv[3], "%d", &delay) != 1) {
    fatal_error("Invalid delay");
  }
  float amp = 0;
  if (sscanf(argv[4], "%f", &amp) != 1) {
    fatal_error("Invalid amplitude");
  }
  //set stero_buf according to the audio file passed in
  FILE *in = fopen(before,"rb");
  if(in == NULL){
    fatal_error("unable to open");
  }
  unsigned num_samples;
  read_wave_header(in, &num_samples);
  int total = (num_samples+delay)*2;
  int16_t *stereo_buf = malloc(sizeof(int16_t)*total);
  int16_t *mono_buf = malloc(sizeof(int16_t)*(2*num_samples));
  read_s16_buf(in, stereo_buf, num_samples*2);
  for (int j = 0; j < (int) (2*num_samples); j++) mono_buf[j] = stereo_buf[j];//store original values to avoid multiple echoes
  for (int i = 0; i<(int) (2*num_samples); i++){
    stereo_buf[i+delay*2] += mono_buf[i]*amp;//make echo
  }
  FILE* output = fopen(after, "wb");
  write_wave_header(output, (num_samples+delay));//output
  write_s16_buf(output, stereo_buf, total);
  fclose(in);
  fclose(output);
  free(mono_buf);
  free(stereo_buf);
  return 0;
}
