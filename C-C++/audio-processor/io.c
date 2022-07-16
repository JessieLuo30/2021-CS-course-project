#include <stdio.h>
#include <stdlib.h>
#include <stdint.h>
#include "io.h"

void fatal_error(const char *message) {
    fprintf(stderr, "Error: %s\n", message);
    exit(1);
}

void write_byte(FILE *out, char val) {
    int check =fwrite(&val, sizeof(char), 1, out);
    if (check!=1){
    fatal_error("File write error...");
}
}

void write_bytes(FILE *out, const char data[], unsigned n) {
    int check =fwrite(data, sizeof(char), n, out);
    if (check!=(int)n){
        fatal_error("File write error...");
    }
}

void write_u16(FILE *out, uint16_t value) {
    int check =fwrite(&value, sizeof(uint16_t), 1, out);
    if (check!=1){
        fatal_error("File write error...");
    }
}

void write_u32(FILE *out, uint32_t value) {
    int check =fwrite(&value, sizeof(uint32_t), 1, out);
    if (check!=1){
        fatal_error("File write error...");
    }
}

void write_s16(FILE *out, int16_t value) {
    int check =fwrite(&value, sizeof(int16_t), 1, out);
    if (check!=1){
        fatal_error("File write error...");
    }
}

void write_s16_buf(FILE *out, const int16_t buf[], unsigned n) {
    write_bytes(out, (const char *) buf, sizeof(int16_t) * n);
}

void read_byte(FILE *in, char *val) {
    int check =fread(val, sizeof(char), 1, in);
    if (check!=1){
        fatal_error("Fail to read the required number of data bytes.");
    }
}
void read_bytes(FILE *in, char data[], unsigned n) {
    int check =fread(data, sizeof(char), n, in);
    if (check!=(int)n){
        fatal_error("Fail to read the required number of data bytes.");
    }
}
void read_u16(FILE *in, uint16_t *val) {
    int check =fread(val, sizeof(uint16_t), 1, in);
    if (check!=1){
        fatal_error("Fail to read the required number of data bytes.");
    }
}
void read_u32(FILE *in, uint32_t *val) {
    int check =fread(val, sizeof(uint32_t), 1, in);
    if (check!=1){
        fatal_error("Fail to read the required number of data bytes.");
    }
}
void read_s16(FILE *in, int16_t *val) {
    int check =fread(val, sizeof(int16_t), 1, in);
    if (check!=1){
        fatal_error("Fail to read the required number of data bytes.");
    }
}

void read_s16_buf(FILE *in, int16_t buf[], unsigned n) {
    for (int i = 0; i < (int)n; i++){
        read_s16(in, buf+i);
    }
}
