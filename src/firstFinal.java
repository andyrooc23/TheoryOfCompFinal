import java.util.*;

class FibonaciSphere{
    public static void main(String[] args) {
        //Generate Coordinates
        List<List<Double>> list = generateCoordinates(20, 5.0);

        for(List<Double> ls : list){
            for(double d : ls){
                System.out.println(d);
            }
            System.out.println();
        }
    }

    public static double Latitude(int i, int n){
        double pi = Math.PI;
        double asin = Math.asin(2.0 * i / (2.0 * n + 1.0));
        if(i < 0){
            return asin;
        }else{
            return pi - asin;
        }
    }

    public static double Longitude(int i){
        return (2.0*Math.PI*i / (1.0 + Math.sqrt(5.0))/2.0);
    }

    public static List<Double> generateX(int n, double r){
        List<Double> a = new ArrayList<>();
        int i = -1 * n;
        while (i <= n){
            a.add(-1.0 * r * Math.cos(Latitude(i, n)) * Math.cos(Longitude(i)));
            i += 1.0;
        }
        return a;
    }

    public static List<Double> generateY(int n, double r){
        List<Double> a = new ArrayList<>();
        int i = -1 * n;
        while (i <= n){
            a.add(r * Math.sin(Latitude(i, n)));
            i += 1;
        }
        return a;
    }

    public static List<Double> generateZ(int n, double r){
        List<Double> a = new ArrayList<>();
        int i = -1 * n;
        while (i <= n){
            a.add(r * Math.cos(Latitude(i, n)) * Math.cos(Longitude(i)));
            i += 1;
        }
        return a;
    }

    public static List<List<Double>> generateCoordinates(int n, double r){
        List<List<Double>> a = new ArrayList<>();
        List<Double> x = generateX(n, r);
        List<Double> y = generateY(n, r);
        List<Double> z = generateZ(n, r);
        int i = 0;
        while(i < x.size()){
            List<Double> b = new ArrayList<>();
            b.add(x.get(i));
            b.add(y.get(i));
            b.add(z.get(i));
            a.add(b);
            i += 1;
        }
        return a;
    }



}