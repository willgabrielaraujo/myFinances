import Axios from "axios";

const httpClient = Axios.create({
  baseURL: "http://localhost:8080",
});

class ApiService {
  constructor(apiUrl) {
    this.apiUrl = apiUrl;
  }

  get(url) {
    const requestUrl = `${this.apiUrl}${url}`;
    return httpClient.get(requestUrl);
  }

  post(url, object) {
    const requestUrl = `${this.apiUrl}${url}`;
    return httpClient.post(requestUrl, object);
  }

  put(url, object) {
    const requestUrl = `${this.apiUrl}${url}`;
    return httpClient.put(requestUrl, object);
  }

  delete(url) {
    const requestUrl = `${this.apiUrl}${url}`;
    return httpClient.delete(requestUrl);
  }
}

export default ApiService;
