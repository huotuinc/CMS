package com.huotu.hotcms.service.service.impl;

import com.huotu.hotcms.service.entity.Host;
import com.huotu.hotcms.service.repository.HostRepository;
import com.huotu.hotcms.service.service.HostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by cwb on 2015/12/24.
 */
@Service
public class HostServiceImpl implements HostService {

    @Autowired
    private HostRepository hostRepository;

    @Override
    public Host getHost(String domain) {
        return hostRepository.findByDomain(domain);
    }

    @Override
    public Boolean save(Host host) {
        hostRepository.save(host);
        return true;
    }

}