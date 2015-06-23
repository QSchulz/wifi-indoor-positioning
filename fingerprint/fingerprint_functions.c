/*
 * Jérôme COUSSANES
 * jerome.coussanes@utbm.fr
 *
 *
 */
#include <stdlib.h>
#include <stdio.h>
#include "fann.h"
#include "floatfann.h"
#include "ogr_api.h"
#include "fingerprint_functions.h"

RCoord fp_to_real(int x, int y){
    RCoord elem;
    elem.x = (x*STEP+MINX);
    elem.y = (y*STEP+MINY);

    return elem;
}

void calc_fp_dimentions(int* x, int* y){
    if((MAXX-MINX)%STEP != 0)
        *x = ((MAXX-MINX)/STEP) + 1;
    else
        *x = (MAXX-MINX)/STEP ;

    if((MAXY-MINY)%STEP != 0)
        *y = ((MAXY-MINY)/STEP) + 1;
    else
        *y = (MAXY-MINY)/STEP ;
}

double calc_distance(RCoord ap, RCoord point){

    double a = ap.x > point.x ? ap.x - point.x : point.x - ap.x;
    double b = ap.y > point.y ? ap.y - point.y : point.y - ap.y;

    return (sqrt(pow(a,2) + pow(b,2))/1000);
}

int calc_wall(RCoord ap, RCoord point){
    int nbWall=0;
    OGRRegisterAll();

    OGRDataSourceH hDS;
    OGRLayerH hLayer;
    OGRFeatureH hFeature;

    OGRGeometryH line = OGR_G_CreateGeometry(wkbLineString);
    OGR_G_AddPoint_2D(line, ap.x, ap.y);
    OGR_G_AddPoint_2D(line, point.x, point.y);

    hDS = OGROpen( "batH.shp", FALSE, NULL );
    if( hDS == NULL )
    {
        printf( "Open failed.\n" );
        exit( 1 );
    }

    hLayer = OGR_DS_GetLayerByName( hDS, "batH" );

    OGR_L_ResetReading(hLayer);
    while( (hFeature = OGR_L_GetNextFeature(hLayer)) != NULL )
    {
        OGRFeatureDefnH hFDefn;
        int iField;
        OGRGeometryH hGeometry;

        hFDefn = OGR_L_GetLayerDefn(hLayer);

        hGeometry = OGR_F_GetGeometryRef(hFeature);
        if( hGeometry != NULL
                && wkbFlatten(OGR_G_GetGeometryType(hGeometry)) == wkbLineString )
        {
            if(OGR_G_Intersects(line, hGeometry)){
                nbWall+=1;
            }
        }

        OGR_F_Destroy( hFeature );
    }

    nbWall/=4;


    OGR_G_DestroyGeometry(line);
    OGR_DS_Destroy( hDS );
    return nbWall;
}

void train_fann(char* in, char* out){
    const unsigned int num_input = 2;
    const unsigned int num_output = 1;
    const unsigned int num_layers = 3;
    const unsigned int num_neurons_hidden = 200;
    const float desired_error = (const float) 50;
    const unsigned int max_epochs = 5;
    const unsigned int epochs_between_reports = 1;

    struct fann *ann = fann_create_standard(num_layers, num_input,
            num_neurons_hidden, num_output);
    fann_set_learning_rate( ann, 0.3 );

    fann_set_activation_function_hidden(ann, FANN_SIGMOID_SYMMETRIC);
    fann_set_activation_function_output(ann, FANN_SIGMOID_SYMMETRIC);

    fann_train_on_file(ann, in, max_epochs,
            epochs_between_reports, desired_error);

    fann_save(ann, out);

    fann_destroy(ann);
}

double aply_fann(char* model, double distance, int wall){
    fann_type *calc_out;
    fann_type input[2];

    input[0] = distance;
    input[1] = wall;

    struct fann *ann = fann_create_from_file(model);

    calc_out = fann_run(ann, input);

    return (double)*calc_out;
}

void fingerprint_generation(){
    int x,y;

    RCoord ap[3];
    ap[0].x = 132;
    ap[0].y = 240;
    ap[1].x = 220;
    ap[1].y = 430;
    ap[2].x = 250;
    ap[2].y = 550;

    calc_fp_dimentions(&x,&y);

    int i,j,k;
    int nbWall=0;
    double distance=0.0,rssi=0.0;

    for(i=0 ; i < x ; i++){
        for(j=0 ; j < y ; j++){
            for(k=0 ; k<3 ; k++){
                nbWall = calc_wall(ap[k], fp_to_real(i,j));
                distance = calc_distance(ap[k], fp_to_real(i,j));

                rssi=aply_fann("fp.net", distance, nbWall);

                //fprint x(i), y(j), vector histogram
            }
        }
    }
}
// TODO gaussian distribution

void create_training(char* measurement, char* output){
    FILE* fmeasure;
    FILE* fout;

    char * line = NULL;
    size_t len = 0;
    ssize_t read;

    RCoord ap, point;
    int x,y;
    double rssi = 4000.0;

    fmeasure = fopen(measurement, "r");
    fout = fopen(output, "w");
    while ((read = getline(&line, &len, fmeasure)) != -1) {
        char* tok;
        if(rssi == 4000.0){
            //fprintf("%d 2 1\n", atoi(line));
            rssi=0;
        }
        else{
            tok = strtok(line, " ");
            if(tok != NULL) x = atoi(tok);
            tok = strtok(NULL," ");
            if(tok != NULL) y = atoi(tok);

            ap = fp_to_real(x,y);

            tok = strtok(NULL," ");
            if(tok != NULL) x = atoi(tok);
            tok = strtok(NULL," ");
            if(tok != NULL) y = atoi(tok);

            point = fp_to_real(x,y);

            tok = strtok(NULL," ");
            if(tok != NULL) rssi = atof(tok);

            //fprintf("%lf %d\n%lf\n",calc_distance(ap, point),calc_wall(ap, point),rssi);
        }

    }

    fclose(fmeasure);
    fclose(fout);
    if (line)
        free(line);
}
