package com.spring.mvc.portfolio.controller;

import com.spring.mvc.portfolio.entities.Investor;
import com.spring.mvc.portfolio.entities.Watch;
import com.spring.mvc.portfolio.service.EmailService;
import com.spring.mvc.portfolio.service.PortfolioService;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/portfolio/investor")
public class InvestorController {

    @Autowired
    private PortfolioService service;

    @Autowired
    private EmailService emailService;

    @GetMapping(value = {"/", "/query"})
    public List<Investor> query() {
        return service.getInvestorRepository().findAll();
    }

    @GetMapping(value = {"/{id}"})
    public Investor get(@PathVariable("id") Optional<Integer> id) {
        Investor investor = service.getInvestorRepository().findOne(id.get());
        return investor;
    }

    @PutMapping(value = {"/{id}"})
    @Transactional
    public Boolean update(@PathVariable("id") Optional<Integer> id, @RequestBody Map<String, String> map) {
        if (get(id) == null) {
            return false;
        }
        service.getInvestorRepository().update(
                id.get(),
                map.get("username"),
                map.get("password"),
                map.get("email"),
                Integer.parseInt(map.get("balance")));
        return true;
    }

    @DeleteMapping(value = {"/{id}"})
    @Transactional
    public Boolean delete(@PathVariable("id") Optional<Integer> id) {
    if(!id.isPresent()){return false;}
    if(get(id)==null){
    return false;
    }
    service.getInvestorRepository().delete(id.get());
    return true;
    }
        @PostMapping(value = {"/", "/add"})
        public Investor add
        (@RequestBody
        Map<String, String> jsomMap
        
            ) {
        Investor investor = new Investor();
            investor.setUsername(jsomMap.get("username"));
            investor.setPassword(jsomMap.get("password"));
            investor.setEmail(jsomMap.get("email"));
            investor.setBalance(Integer.parseInt(jsomMap.get("balance")));
            investor.setPass(Boolean.FALSE);
            //設定認證碼
            investor.setCode(Integer.toHexString(investor.hashCode()));
            //存檔 Investor
            service.getInvestorRepository().save(investor);
            //存檔 Watch 
            Watch watch = new Watch(investor.getUsername() + "的投資組合", investor);
            service.getWatchRepository().save(watch);
            //發送認證信
            emailService.send(investor);
            return investor;
        }
    }
