package com.example.demo.Service;

import org.springframework.stereotype.Service;

import com.example.demo.Model.Url;
import com.example.demo.Model.UrlDto;

@Service
public interface UrlService {

	public Url generateShortLink(UrlDto urlDto);
	public Url persistShortLink(Url url);
	public Url getEncodedUrl(String url);
	public void deleteShortLink(Url url);
}
