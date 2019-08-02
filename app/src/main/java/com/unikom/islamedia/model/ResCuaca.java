package com.unikom.islamedia.model;

import java.util.List;

public class ResCuaca {

    List<IconCuaca> weather;
    private CuacaModel main;

    public ResCuaca(CuacaModel main) {
        this.main = main;
    }

    public CuacaModel getMain() {
        return main;
    }

    public void setMain(CuacaModel main) {
        this.main = main;
    }

    public List<IconCuaca> getWeather() {
        return weather;
    }

    public void setWeather(List<IconCuaca> weather) {
        this.weather = weather;
    }
}
