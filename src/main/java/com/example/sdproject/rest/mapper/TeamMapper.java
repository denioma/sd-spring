package com.example.sdproject.rest.mapper;

import com.example.sdproject.data.entity.Team;
import kong.unirest.json.JSONObject;

public class TeamMapper {
    private final JSONObject team;
    public TeamMapper(JSONObject jsonObject) {
        this.team = jsonObject.getJSONObject("team");
    }

    public Team map() {
        Team t = new Team();
        t.setTeamId(team.getInt("id"));
        t.setName(team.getString("name"));
        t.setImage(team.getString("logo"));
        return t;
    }
}
