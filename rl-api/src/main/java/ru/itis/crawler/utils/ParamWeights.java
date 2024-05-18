package ru.itis.crawler.utils;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ParamWeights {

    public static final Integer NAME = 20;
    public static final Integer NAME_CODE = 30;
    public static final Integer DESCRIPTION = 1;
    public static final Integer PRICE = 10;
    private String polarity;
    private Integer voltage;
    private Integer inrushCurrent;
    private Integer capacity;
    private String typeOfVehicle;
    private String batteryTechnology;
    private String features;
    private String klemmy;
    private String caseType;
    private String batteryMarking;
    private Double length;
    public static final Integer WIDTH = 50;
    public static final Integer HEIGHT = 50;
    public static final Integer WEIGHT = 50;
    public static final Integer LENGTH = 50;
    private Integer serviceLife;
}
