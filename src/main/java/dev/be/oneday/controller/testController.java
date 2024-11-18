package dev.be.oneday.controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import dev.be.oneday.dto.Request.HabitRequest;
import dev.be.oneday.service.KeywordMongoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

@RestController
@RequestMapping("/mongo-test")
@RequiredArgsConstructor
public class testController {
    private final KeywordMongoService keywordMongoService;

    @PostMapping("/{habitId}")
    public void insert(
            @RequestBody HabitRequest habitRequest,
            @PathVariable Long habitId
    ){
        keywordMongoService.analysisAndSave(habitRequest.getTitle(),habitId);
    }

    @DeleteMapping
    public void deleteAll(){
        keywordMongoService.deleteAll();
    }

    @PutMapping("/{habitId}")
    public void update(
            @RequestParam String before,
            @RequestParam String after,
            @PathVariable Long habitId
    ){
        keywordMongoService.analysisAndUpdate(before,after,habitId);
    }

    class PageGame{
        int page;
        int per_page;
        int total;
        int total_pages;
        Game[] data;
        class Game{
            String competition;
            int year;
            String round;
            String team1;
            String team2;
            String team1goals;
            String team3goals;
        }
    }
    @RequestMapping("/t")
    public void test() throws Exception{
        Gson gson = new Gson();
        JsonObject jsonObject = new JsonObject();

        StringBuilder sb = new StringBuilder();
        int page=1;
        String team = "Barcelona";
        int year=2011;
        sb.append("https://jsonmock.hackerrank.com/api/football_matches?year=").append(year);
        sb.append("&team1=").append(team);
        sb.append("&page=").append(page);

        URL url = new URL(sb.toString());
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String json = br.readLine();
        PageGame p = gson.fromJson(json, PageGame.class);
        System.out.println(p.page+" "+p.data[0].team1);
    }
}
