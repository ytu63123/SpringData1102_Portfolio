package com.spring.mvc.portfolio.controller;

import com.spring.mvc.portfolio.entities.TStock;
import com.spring.mvc.portfolio.entities.Watch;
import com.spring.mvc.portfolio.repository.TStockRepository;
import com.spring.mvc.portfolio.repository.WatchRepository;
import com.spring.mvc.portfolio.service.PortfolioService;
import java.util.List;
import java.util.Map;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/portfolio/watch")
public class WatchController {
    
      @Autowired
    private PortfolioService service;
    
    @GetMapping(value = {"/{id}", "/get/{id}"})
    @Transactional
    public Watch get(@PathVariable("id") Integer id) {
        return service.getWatchRepository().findOne(id);
    }
    
    @GetMapping(value = {"/", "/query"})
    public Iterable<Watch> query() {
        return service.getWatchRepository().findAll();
    }
    
     @PutMapping(value = {"/{id}"}) 
    @Transactional
    public Boolean update(@PathVariable("id") Integer id, @RequestBody Map<String, String> map) {
        Watch watch = get(id);
        if (watch == null) return false;
        
        watch.setName(map.get("name"));
        service.getWatchRepository().save(watch);
        return true;
    }
    
    @GetMapping(value = {"/{id}/add/{tstock_id}"})
    @Transactional
    public Watch add_tstock(@PathVariable("id") Integer id, @PathVariable("tstock_id") Integer tstock_id) {
        Watch watch = get(id);
        TStock ts = service.gettStockRepository().findOne(tstock_id);
        watch.addtStock(ts);
        service.getWatchRepository().save(watch);
        return get(id);
    }
    
    @DeleteMapping(value = {"/{id}/remove/{tstock_id}"})
    @Transactional
    public Watch remove_tstock(@PathVariable("id") Integer id, @PathVariable("tstock_id") Integer tstock_id) {
        Watch watch = get(id);
        TStock ts = service.gettStockRepository().findOne(tstock_id);
        watch.removetStock(ts);
        service.getWatchRepository().save(watch);
        return get(id);
    }
    
}
