package com.nttdataTest.dao;

import org.springframework.stereotype.Repository;

import com.nttdataTest.model.User;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.data.repository.CrudRepository;

@Repository
@Configurable
public interface UsersDAO extends CrudRepository<User, Long>{}

