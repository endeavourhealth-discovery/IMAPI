package org.endeavourhealth.zfhirmapper;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ZMapperCommon {
    public static void main(String[] argv) throws Exception {
        System.out.println("test");
    }

    public static Integer CountPieces(String str, String del)
    {
        String[] split = str.split(del);
        return split.length;
    }

    public static boolean indexInBound(String[] data, int index) {
        return data != null && index >= 0 && index < data.length;
    }

    public static String Piece(String str, String del, Integer from, Integer to) {
        Integer i;

        // if (!str.contains(del)) {return str;}

        String p[] = str.split(del, -1);
        String z = "";

        from = from - 1;
        to = to - 1;

        Integer zdel = 0;
        if (to > from) {
            zdel = 1;
        }

        for (i = from; i <= to; i++) {
            if (indexInBound(p, i)) {
                z = z + p[i];
                if (zdel.equals(1)) {
                    z = z + del;
                }
            }
        }

        if (zdel.equals((1)) && !z.isEmpty()) {
            // remove delimeter
            z = z.substring(0, z.length() - del.length());
        }

        return z;
    }

    public float toFloat(String Str)
    {
        float v;
        try {
            v = (Float.valueOf(Str).floatValue());
        } catch (NumberFormatException e) {
            v = -1;
        }
        return v;
    }

    public static String justify(String str)
    {
        // ("00000000" + "Apple").substring("Apple".length())
        if (str.length()>2) return str;
        str = ("00"+str).substring(str.length());
        return str;
    }

    public static String HD(Integer date)
    {
        String z = "";
        if (date>21914) date = date+1;
        Integer leap = Math.round(date/1461);

        Integer r = date % 1461;

        Integer year = leap*4+1841+(Math.round(r/365));
        Integer day = r % 365;
        Integer month = 1;

        if (r == 1460 && leap != 14) {day=365; year=year-1;}

        Integer x = 28;
        if (r>1154 && leap !=14) {x=29;}

        Integer var[] = {31,x,31,30,31,30,31,31,30,31,30};
        for(int i=0; i<var.length ;i++) {
            Integer loop = var[i];
            if (loop>day || loop == day) break;
            month = month+1;
            day = day-loop;
        }

        if (day.equals(0)) {year=year-1; month=12; day=31;}

        z = justify(year.toString())+"-"+justify(month.toString())+"-"+justify(day.toString());

        return z;
    }

    public static Integer DH(String date)
    {
        String year = Piece(date, "-", 1, 1);
        String day = Piece(date, "-", 3, 3);
        String month = Piece(date, "-", 2, 2);

        Integer y = Integer.parseInt(year);
        Integer d = Integer.parseInt(day);
        Integer m = Integer.parseInt(month);

        Integer r = Math.round((y-1)/4)-(Math.round((y-1)/100))+(Math.round((y-1)/400))-446;

        Integer ret = 366*r+((y-1841-r)*365)+d;

        Integer leap = 29;

        if (y%4>0) { leap = 28;}
        else if (y%100>0) {leap = 29;}
        else if (y%400>0) {leap = 28;}

        Integer var[] = {31,leap,31,30,31,30,31,31,30,31,30,31};
        for(int i=0; i<var.length ;i++) {
            m = m - 1;
            if (m.equals(0)) break;
            ret = ret + var[i];
        }

        return ret;
    }

    public static String FormatDate(Date pDate) throws Exception
    {

        String formatedDate = pDate.toString();

        DateFormat formatter = new SimpleDateFormat("EEE MMM dd HH:mm:ss Z yyyy");
        Date date = (Date)formatter.parse(formatedDate);

        Calendar cal = Calendar.getInstance();
        cal.setTime(date);

        Integer mm = (cal.get(Calendar.MONTH)+1);
        String mmStr =mm.toString();

        Integer dd = cal.get(Calendar.DATE);
        String ddStr = dd.toString();

        //formatedDate = cal.get(Calendar.YEAR) + "-" + (cal.get(Calendar.MONTH)+1) + "-" + cal.get(Calendar.DATE);
        formatedDate = cal.get(Calendar.YEAR) + "-" + justify(mmStr) + "-" + justify(ddStr);

        return formatedDate;
    }
}
