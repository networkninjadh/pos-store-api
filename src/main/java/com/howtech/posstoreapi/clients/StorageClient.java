package com.howtech.posstoreapi.clients;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

@Component
public class StorageClient {

  private final RestTemplate restTemplate;
  private final HttpHeaders headers = new HttpHeaders();
  
  String URL = "http://localhost:";
  
  public StorageClient(RestTemplate restTemplate) {
    this.restTemplate = restTemplate;
    
    headers.add("Content-Type", "application/json");
  }
  
  public String uploadStoreLogo(Long storeId, MultipartFile file, String username) {
    
    headers.add("user-token", username);
    
    String UPLOAD = "/storage/store-profile/" + storeId;
    
    MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
    
    body.add("file", file);
    
    HttpEntity<MultiValueMap<String, Object>> request  =
       new HttpEntity<>(body, headers);
    
    ResponseEntity<String> response = restTemplate
      .postForEntity(URL + UPLOAD, request, String.class);
    
    return response.getBody();
  }
  
  public String getStoreLogoUrl(Long storeId, String username) {
  
    headers.add("user-token", username);
    
    String POST_URL = "/storage/store-profile/" + storeId;
    
    HttpEntity<String> request = new HttpEntity<>("", headers);
    
    ResponseEntity<String> response = restTemplate
      .postForEntity(URL + POST_URL, request, String.class);
    
    return response.getBody();
  }
  
  public String deleteStoreLogo(Long storeId, String username) {
    
    headers.add("user-token", username);
    
    String DELETE = "/storage/store-profile/delete/" + storeId;
    
    HttpEntity<String> request = new HttpEntity<>("", headers);
    
    ResponseEntity<String> response = restTemplate
      .postForEntity(URL + DELETE, request, String.class);
    
    return response.getBody();
  }
  
  public String uploadInventoryPhoto(Long storeId, Long inventoryId, MultipartFile file, String username) {
  
    headers.add("user-token", username);
    
    String UPLOAD = "/storage/store-profile/" + storeId + "/inventory-item/" + inventoryId;
    
    MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
    
    body.add("file", file);
    
    HttpEntity<MultiValueMap<String, Object>> request = 
       new HttpEntity<>(body, headers);
    
    ResponseEntity<String> response = restTemplate.
      postForEntity(URL + UPLOAD, request, String.class);
    
    return response.getBody();
  }
  
  public String getInventoryPhotoUrl(Long storeId, Long inventoryId, String username) {
    
    headers.add("user-token", username);
    
    String GET_URL = "/storage/store-profile/" + storeId + "/inventory-item/" + inventoryId;
    
    HttpEntity<String> request = new HttpEntity<>("", headers);
    
    ResponseEntity<String> response = restTemplate
      .postForEntity(URL + GET_URL, request, String.class);

    return response.getBody();
  }
  
}
