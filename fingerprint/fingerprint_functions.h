/*
 * Jérôme COUSSANES
 * jerome.coussanes@utbm.fr
 *
 *
 */

#ifndef DEF_FUNCTION
#define DEF_FUNCTION

#define MINX     123.00
#define MINY     210.00
#define MAXX     287.00
#define MAXY     607.00
#define STEP        1.5

typedef struct{
    double x;
    double y;
}RCoord;

RCoord fp_to_real(int x, int y);

void calc_fp_dimentions(int* x, int* y);

double calc_distance(RCoord ap, RCoord point);

int calc_wall(RCoord ap, RCoord point);

void train_fann(char* in, char* out);

double aply_fann(char* model, double distance, int wall);

void fingerprint_generation();

void create_training(char* measurement, char* output);

#endif
