package com.example.demo.Service;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.demo.Model.Url;
import com.example.demo.Model.UrlDto;
import com.example.demo.Repository.UrlRepository;
import com.google.common.hash.Hashing;

import io.micrometer.common.util.StringUtils;

@Component
public class UrlServiceImpl implements UrlService {

	@Autowired
	private UrlRepository urlRepository;
	
	@Override
	public Url generateShortLink(UrlDto urlDto) {
		if(StringUtils.isNotEmpty(urlDto.getUrl())) {
			String encodedUrl = encodeUrl(urlDto.getUrl());
			Url urlToPersist = new Url();
			urlToPersist.setCreationDate(LocalDateTime.now());
			urlToPersist.setOriginalUrl(urlDto.getUrl());
			urlToPersist.setShortLink(encodedUrl);
			urlToPersist.setExpiryDate(getExpiryDate(urlDto.getExpiryDate(), urlToPersist.getCreationDate()));
			Url urlToRet = persistShortLink(urlToPersist);
			
			if(urlToRet != null)
				return urlToRet;
			return null;
		}
		return null;
	}

	private LocalDateTime getExpiryDate(String expiryDate, LocalDateTime creationDate) {
		if(StringUtils.isBlank(expiryDate)) {
			return creationDate.plusSeconds(60);
		}
		LocalDateTime expiryDateToRet = LocalDateTime.parse(expiryDate);
		return expiryDateToRet;
	}

	private String encodeUrl(String url) {
		String encodedUrl = "";
		LocalDateTime time = LocalDateTime.now();
		//generate encoded url using hashing, concatenated with time to avoid duplicate
		encodedUrl = Hashing.murmur3_32_fixed()
				.hashString(url.concat(time.toString()), StandardCharsets.UTF_8)
				.toString();
		return encodedUrl;
	}

	@Override
	public Url persistShortLink(Url url) {
		return urlRepository.save(url);
	}

	@Override
	public Url getEncodedUrl(String url) {
		return urlRepository.findByShortLink(url);
	}

	@Override
	public void deleteShortLink(Url url) {
		urlRepository.delete(url);
	}

}
