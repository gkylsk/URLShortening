package com.example.demo.Controller;

import java.io.IOException;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.Model.Url;
import com.example.demo.Model.UrlDto;
import com.example.demo.Model.UrlErrorResponseDto;
import com.example.demo.Model.UrlResponseDto;
import com.example.demo.Service.UrlService;
import io.micrometer.common.util.StringUtils;
import jakarta.servlet.http.HttpServletResponse;

@RestController
public class UrlController {

	@Autowired
	private UrlService urlService;
	
	@PostMapping("/generate")
	public ResponseEntity<?> generateShortLink(@RequestBody UrlDto urlDto) {
		Url url = urlService.generateShortLink(urlDto);
		
		if(url != null) {
			UrlResponseDto urlResponseDto = new UrlResponseDto();
			urlResponseDto.setOriginalUrl(url.getOriginalUrl());
			urlResponseDto.setExpiryDate(url.getExpiryDate());
			urlResponseDto.setShortLink(url.getShortLink());
			return new ResponseEntity<UrlResponseDto>(urlResponseDto, HttpStatus.OK);
		}
		
		return errorResponse("404","There was an error processing your request. Please try again!");
	}
	
	@GetMapping("/{shortLink}")
	public ResponseEntity<?> redirectToOriginalUrl(@PathVariable String shortLink, HttpServletResponse response) throws IOException {
		//check if url is empty
		if(StringUtils.isEmpty(shortLink)) {
			return errorResponse("400","Invalid Url");
		}
		
		Url url = urlService.getEncodedUrl(shortLink);
		
		//check if url exist in database
		if(url == null) {
			return errorResponse("400","Url does not exist or expired!");
		}
		
		//check if url has expired
		if(url.getExpiryDate().isBefore(LocalDateTime.now())) {
			urlService.deleteShortLink(url);
			return errorResponse("200","Url Expired. Please generate a new Url!");
		}
		
		response.sendRedirect(url.getOriginalUrl());
		return null;
		
	}
	
	private ResponseEntity<UrlErrorResponseDto> errorResponse(String req, String error){
		UrlErrorResponseDto urlErrorResponseDto = new UrlErrorResponseDto();
		urlErrorResponseDto.setStatus(req);
		urlErrorResponseDto.setError(error);
		return new ResponseEntity<UrlErrorResponseDto>(urlErrorResponseDto, HttpStatus.OK);
	}
	@DeleteMapping
	public void delete(Url shortLink) {
		urlService.deleteShortLink(shortLink);
	}
}
