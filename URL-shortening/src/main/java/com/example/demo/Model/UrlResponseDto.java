package com.example.demo.Model;

import java.time.LocalDateTime;

public class UrlResponseDto {

	private String originalUrl;
	private String shortLink;
	private LocalDateTime expiryDate;
	
	
	public UrlResponseDto() {
		super();
	}
	public UrlResponseDto(String originalUrl, String shortLink, LocalDateTime expiryDate) {
		super();
		this.originalUrl = originalUrl;
		this.shortLink = shortLink;
		this.expiryDate = expiryDate;
	}
	public String getOriginalUrl() {
		return originalUrl;
	}
	public void setOriginalUrl(String originalUrl) {
		this.originalUrl = originalUrl;
	}
	public String getShortLink() {
		return shortLink;
	}
	public void setShortLink(String shortLink) {
		this.shortLink = shortLink;
	}
	public LocalDateTime getExpiryDate() {
		return expiryDate;
	}
	public void setExpiryDate(LocalDateTime expiryDate) {
		this.expiryDate = expiryDate;
	}
	@Override
	public String toString() {
		return "UrlResponseDto [originalUrl=" + originalUrl + ", shortLink=" + shortLink + ", expiryDate=" + expiryDate
				+ "]";
	}
	
}
