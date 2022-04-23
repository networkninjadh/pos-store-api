package com.howtech.posstoreapi.clients;

import org.springframework.stereotype.Component;

@Component
public class StorageClient {

  private final RestTemplate restTemplate;
  private final HttpHeaders headers = new HttpHeaders();
  
  String URL = "http://localhost:";
  
  public StorageClient(RestTemplate restTemplate) {
    this.restTemmplate = restTemplate;
    
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
      .postForEntity(URL + UPLOAD, String.class);
    
    return response.getBody();
  }
  
  public String getStoreLogoUrl(Long storeId, String username) {
  
    headers.add("user-token", username);
    
    String GET_URL = "/storage/store-profile/" + storeId;
    
    HttpEntity<void> request = new HttpEntity<>(headers);
    
    ResponseEntity<String> response = restTemplate
      .getForEntity(URL, request, String.class);
    
    return response.getBody();
  }
  
  public String deleteStoreLogo(Long storeId, username) {
    
    headers.add("user-token", username);
    
    String DELETE = "/storage/store-profile/delete/" + storeId;
    
    HttpEntity<void> request = new HttpEntity<>(headers);
    
    ResponseEntity<String> response = restTemplate
      .delete(URL, request, String.class);
    
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
      postForEntity(URL + UPLOAD, String.class);
    
    return response.getBody();
  }
  
  public String getInventoryPhotoUrl(Long storeId, Long inventoryId, String username) {
    
    headers.add("user-token", username);
    
    String GET_URL = "/storage/store-profile/" + storeId + "/inventory-item/" + inventoryId;
    
    HttpEntity<void> request = new HttpEntity<>(headers);
    
    ResponseEntity<String> response = restTemplate
      .getForEntity(URL, request, String.class);
  }
  
}
