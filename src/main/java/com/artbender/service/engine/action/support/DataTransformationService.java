package com.artbender.service.engine.action.support;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Setter
@Getter
@Service
@Scope("prototype")
public class DataTransformationService {

    public List<Integer> getIntListDate(List<String> data) {
        List<Integer> results = new ArrayList<>();
        data.forEach(row -> results.add(Integer.valueOf(row)));
        return results;
    }

    public List<Double> getListDateDouble(List<String> data) {
        List<Double> results = new ArrayList<>();
        data.forEach(row -> results.add(Double.valueOf(row)));
        return results;
    }

    public OnBall getNextOnBall(OnBall onBall) {
        return onBall == OnBall.AWAY ? OnBall.HOME : OnBall.AWAY;
    }
}
