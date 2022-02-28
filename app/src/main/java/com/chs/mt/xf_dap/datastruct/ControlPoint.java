package com.chs.mt.xf_dap.datastruct;

import android.graphics.PointF;

import java.util.ArrayList;
import java.util.List;

public class ControlPoint{
        private PointF conPoint1;
        private PointF conPoint2;

        public ControlPoint(PointF p1, PointF p2){
            this.conPoint1 = p1;
            this.conPoint2 = p2;
        }

        public PointF getConPoint1() {
            return conPoint1;
        }

        public void setConPoint1(PointF conPoint1) {
            this.conPoint1 = conPoint1;
        }

        public PointF getConPoint2() {
            return conPoint2;
        }

        public void setConPoint2(PointF conPoint2) {
            this.conPoint2 = conPoint2;
        }

        public static List<ControlPoint> getControlPointList(List<PointF> pointFs){
            List<ControlPoint> controlPoints = new ArrayList<>();


            float conP1x;
            float conP1y;
            float conP2x;
            float conP2y;
            PointF p0=null;
            PointF p1=null;
            PointF p2=null;
            PointF p3=null;

            for (int i=1; i<pointFs.size();i++){

                float smooth_value = 0.7f;
                float ctrl1_x;
                float ctrl1_y;
                float ctrl2_x;
                float ctrl2_y;


                    if(i==1){
                        p0=pointFs.get(i);
                        p1=pointFs.get(i-1);
                        p2=pointFs.get(i);
                        p3=pointFs.get(i+1);
                    }else if(i==2){
                        p0=pointFs.get(i-2);
                        p1=pointFs.get(i-1);
                        p2=pointFs.get(i);
                        p3=pointFs.get(i+1);
                    }else if((i+1)<pointFs.size()){
                        p0=pointFs.get(i-2);
                        p1=pointFs.get(i-1);
                        p2=pointFs.get(i);
                        p3=pointFs.get(i+1);
                    }else{
                            p0=pointFs.get(i-1);
                            p1=pointFs.get(i);
                            p2=pointFs.get(i-1);
                            p3=pointFs.get(i);
                    }



                float xc1 = (float) ((p0.x + p1.x) /1.5);
                float yc1 = (float) ((p0.y + p1.y) /1.5);
                float xc2 = (float) ((p1.x + p2.x) /1.5);
                float yc2 = (float) ((p1.y + p2.y) /1.5);
                float xc3 = (float) ((p2.x + p3.x) /1.5);
                float yc3 = (float) ((p2.y + p3.y) /1.5);
                float len1 = (float) Math.sqrt((p1.x-p0.x) * (p1.x-p0.x) + (p1.y-p0.y) * (p1.y-p0.y));
                float len2 = (float) Math.sqrt((p2.x-p1.x) * (p2.x-p1.x) + (p2.y-p1.y) * (p2.y-p1.y));
                float len3 = (float) Math.sqrt((p3.x-p2.x) * (p3.x-p2.x) + (p3.y-p2.y) * (p3.y-p2.y));
                float k1 = len1 / (len1 + len2);
                float k2 = len2 / (len2 + len3);
                float xm1 = xc1 + (xc2 - xc1) * k1;
                float ym1 = yc1 + (yc2 - yc1) * k1;
                float xm2 = xc2 + (xc3 - xc2) * k2;
                float ym2 = yc2 + (yc3 - yc2) * k2;
                ctrl1_x = xm1 + (xc2 - xm1) * smooth_value + p1.x - xm1;
                ctrl1_y = ym1 + (yc2 - ym1) * smooth_value + p1.y - ym1;
                ctrl2_x = xm2 + (xc2 - xm2) * smooth_value + p2.x - xm2;
                ctrl2_y = ym2 + (yc2 - ym2) * smooth_value + p2.y - ym2;

                p1 = new PointF(ctrl1_x,ctrl1_y);
                p2 = new PointF(ctrl2_x,ctrl2_y);


//                if (i == 0){
//                    //第一断1曲线 控制点
//                    conP1x = pointFs.get(i).x + (pointFs.get(i + 1).x-pointFs.get(i).x)/4;
//                    conP1y = pointFs.get(i).y + (pointFs.get(i + 1).y-pointFs.get(i).y)/4;
//
//                    conP2x = pointFs.get(i+1).x - (pointFs.get(i + 2).x - pointFs.get(i).x)/4;
//                    conP2y = pointFs.get(i+1).y - (pointFs.get(i + 2).y - pointFs.get(i).y)/4;
//
//                }else if (i == pointFs.size() - 2){
//                    //最后一段曲线 控制点
//                    conP1x = pointFs.get(i).x + (pointFs.get(i + 1).x-pointFs.get(i - 1).x)/4;
//                    conP1y = pointFs.get(i).y + (pointFs.get(i + 1).y-pointFs.get(i - 1).y)/4;
//
//                    conP2x = pointFs.get(i+1).x - (pointFs.get(i + 1).x - pointFs.get(i).x)/4;
//                    conP2y = pointFs.get(i+1).y - (pointFs.get(i + 1).y - pointFs.get(i).y)/4;
//                }else {
//                    conP1x = pointFs.get(i).x + (pointFs.get(i + 1).x-pointFs.get(i - 1).x)/4;
//                    conP1y = pointFs.get(i).y + (pointFs.get(i + 1).y-pointFs.get(i - 1).y)/4;
//
//                    conP2x = pointFs.get(i+1).x - (pointFs.get(i + 2).x - pointFs.get(i).x)/4;
//                    conP2y = pointFs.get(i+1).y - (pointFs.get(i + 2).y - pointFs.get(i).y)/4;
//                }
//
//                p1 = new PointF(conP1x,conP1y);
//                p2 = new PointF(conP2x,conP2y);

                ControlPoint controlPoint = new ControlPoint(p1, p2);
                controlPoints.add(controlPoint);
            }

            return controlPoints;
        }


        private List<ControlPoint> setPointAll(){
            List<ControlPoint> controlPoints = new ArrayList<>();



            return controlPoints;
        }

}
