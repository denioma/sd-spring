package com.example.sdproject.rest.mapper;

import com.example.sdproject.data.entity.League;
import com.example.sdproject.data.service.LeagueService;
import kong.unirest.json.JSONObject;
import org.springframework.stereotype.Component;

@Component
public class LeagueMapper {
    private final LeagueService leagueService;

    public LeagueMapper(LeagueService leagueService) {
        this.leagueService = leagueService;
    }

    public League map(JSONObject json) {
        JSONObject league = json.getJSONObject("league");
        JSONObject country = json.getJSONObject("country");
        League l = new League();
        l.setLeagueId(league.getInt("id"));
        l.setName(league.getString("name"));
        l.setCountry(country.getString("name"));
        return l;
    }
}
