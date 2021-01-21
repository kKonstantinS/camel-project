package com.example.camel;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.Repository;

import java.util.List;


public interface MessageRepository extends Repository<Message, Long>, CrudRepository<Message, Long> {

    List<Message> findAll();

    Message findFirstByStatusEquals(String status);
}
