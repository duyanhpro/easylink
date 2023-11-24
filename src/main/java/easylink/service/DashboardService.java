package easylink.service;

import easylink.entity.Dashboard;
import easylink.repository.DashboardRepository;
import easylink.util.BeanUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DashboardService {
    @Autowired
    DashboardRepository repo;
    public Dashboard save(Dashboard d) {
        return repo.save(d);
    }
    public void delete(int id) {
        repo.deleteById(id);
    }
    public List<Dashboard> findAll() {
        return repo.findAll();
    }
    public Dashboard findById(int id) {
        return repo.findById(id).get();
    }

    public Dashboard update(Integer id, Dashboard dashboard) {
        Dashboard d = findById(id);
        BeanUtil.merge(d, dashboard);
        repo.save(d);
        return d;
    }
}
