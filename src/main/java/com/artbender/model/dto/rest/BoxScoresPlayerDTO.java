package com.artbender.model.dto.rest;


import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(builderClassName = "Builder")
public class BoxScoresPlayerDTO {

    private int id;
    private String name;
    private boolean inStart;
    private String currentGamePosition;
    private String stamina;
    private String minutes;
    private String _FG;
    private String _3P;
    private String _FT;
    private int _REB;
    private int _OFFR;
    private int _DEFR;
    private int _AST;
    private int _PF;
    private int _ST;
    private int _TO;
    private int _BS;
    private int _PTS;
}
