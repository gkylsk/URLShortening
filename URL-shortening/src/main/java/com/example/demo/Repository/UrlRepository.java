package com.example.demo.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.Model.Url;

@Repository
public interface UrlRepository extends JpaRepository<Url, Long>{

	public Url findByShortLink(String shortLink);
}
