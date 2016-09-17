package com.wyb.code.spinnerthreelevel;

import android.content.res.XmlResourceParser;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private Spinner spProvince, spCity, spCounty;
    private List<String> provinceNameList;
    private List<String> provinceIdList;
    private ArrayAdapter<String> provinceAdapter;
    private ArrayAdapter<String> cityAdapter;
    private ArrayAdapter<String> countyAdapter;
    private List<String> cityNameList;
    private List<String> cityIdList;
    private List<String> countyNameList;
    private Object provinceListFromXml;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();

        getProvinceListFromXml();

        setListener();
        setAdapter();
    }

    private void setAdapter() {
        provinceAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, provinceNameList);
        provinceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spProvince.setAdapter(provinceAdapter);

        cityAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
        cityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCity.setAdapter(cityAdapter);

        countyAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
        countyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCounty.setAdapter(countyAdapter);
    }

    private void setListener() {
        spProvince.setOnItemSelectedListener(this);
        spCity.setOnItemSelectedListener(this);
        spCounty.setOnItemSelectedListener(this);
    }

    private void initView() {
        spProvince = (Spinner) findViewById(R.id.sp_province);
        spCity = (Spinner) findViewById(R.id.sp_city);
        spCounty = (Spinner) findViewById(R.id.sp_county);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
        switch (adapterView.getId()) {
            case R.id.sp_province:
                String provinceId = provinceIdList.get(position);
                getCityListFromXml(provinceId);

                cityAdapter.clear();
                cityAdapter.addAll(cityNameList);

                String cityId = cityIdList.get(0);
                getCountyListFromXml(cityId);

                countyAdapter.clear();
                countyAdapter.addAll(countyNameList);

                spCity.setSelection(0);
                spCounty.setSelection(0);
                break;
            case R.id.sp_city:
                String cityId2 = cityIdList.get(position);
                getCountyListFromXml(cityId2);

                countyAdapter.clear();
                countyAdapter.addAll(countyNameList);
                break;
            case R.id.sp_county:
                break;
        }
    }



    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }


    private void getProvinceListFromXml() {
        XmlResourceParser parser = getResources().getXml(R.xml.citys);
        try {
            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        provinceNameList = new ArrayList<>();
                        provinceIdList = new ArrayList<>();
                        break;
                    case XmlPullParser.START_TAG:
                        String nodeName = parser.getName();
                        if ("p".equals(nodeName)) {
                            String provinceId = parser.getAttributeValue(0);
                            provinceIdList.add(provinceId);
                            parser.next();
                            String provinceName = parser.nextText();
                            provinceNameList.add(provinceName);
                        }
                        break;
                }
                eventType = parser.next();
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void getCityListFromXml(String provinceId) {
        XmlResourceParser parser = getResources().getXml(R.xml.citys);
        try {
            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        cityNameList = new ArrayList<>();
                        cityIdList = new ArrayList<>();
                        break;
                    case XmlPullParser.START_TAG:
                        String nodeName = parser.getName();
                        if ("c".equals(nodeName)) {
                            String cityId = parser.getAttributeValue(0);
                            if (cityId.substring(0, 2).indexOf(provinceId) == 0) {
                                parser.next();
                                String cityName = parser.nextText();
                                cityIdList.add(cityId);
                                cityNameList.add(cityName);
                            }
                        }
                        break;
                }
                eventType = parser.next();
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void getCountyListFromXml(String cityId) {
        XmlResourceParser parser = getResources().getXml(R.xml.citys);
        try {
            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        countyNameList = new ArrayList<>();
                        break;
                    case XmlPullParser.START_TAG:
                        String nodeName = parser.getName();
                        if ("d".equals(nodeName)) {
                            String countyId = parser.getAttributeValue(0);
                            //直辖市
                            if ("0101".equals(cityId) || "0201".equals(cityId) || "0301".equals(cityId) || "0401".equals(cityId)) {
                                while (countyId.substring(3).indexOf(cityId.substring(0, 2)) == 0) {
                                    String countyName = parser.nextText();
                                    countyNameList.add(countyName);
                                    parser.next();
                                }
                            }

                            if (countyId.substring(3).indexOf(cityId) == 0){
                                String countyName = parser.nextText();
                                countyNameList.add(countyName);
                            }
                        }
                        break;
                }
                eventType = parser.next();
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
