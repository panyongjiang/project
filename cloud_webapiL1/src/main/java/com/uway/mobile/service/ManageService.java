package com.uway.mobile.service;

import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface ManageService {
    public String add(String msg) throws Exception;
    public String delete();
}
