package com.gp.job;

import com.gp.domain.entity.Article;
import com.gp.service.ArtivleService;
import com.gp.utils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class UpdateViewCountJob {

    @Autowired
    private RedisCache redisCache;
    @Autowired
    private ArtivleService artivleService;

    @Scheduled(cron = "0/30 * * * * ?")
    public void  updayeViewCount(){
        Map<String, Integer> viewCount = redisCache.getCacheMap("ViewCount");
        List<Article> collect = viewCount.entrySet().stream()
                .map(entry -> new Article(Long.valueOf(entry.getKey()), entry.getValue().longValue()))
                .collect(Collectors.toList());
        artivleService.updateBatchById(collect);
    }
}
