import axios from "axios";
import { Toast } from "vant";
import store from "@/store/store";

// create an axios instance
const service = axios.create({
  baseURL: process.env.VUE_APP_BASE_API, // url = base url + request url
  withCredentials: true, // send cookies when cross-domain requests
  timeout: 5000 // request timeout
});

// request interceptor
service.interceptors.request.use(
  config => {
    // do something before request is sent

    console.log(config.data);
    Toast.loading({forbidClick: true,})
    return config;
  },
  error => {
    // do something with request error
    console.log(error, "err"); // for debug
    Toast.clear()
    return Promise.reject(error);
  }
);

// response interceptor
service.interceptors.response.use(
  /**
   * If you want to get http information such as headers or status
   * Please return  response => response
   */

  /**
   * Determine the request status by custom code
   * Here is just an example
   * You can also judge the status by HTTP Status Code
   */
  response => {
    const res = response.data;
    Toast.clear()
    // if the custom code is not 20000, it is judged as an error.
    if (response.status != 200) {
      return Promise.reject(
        new Error(res.message || res.data.message || "Error")
      );
    } else {
      return res;
    }
  },
  error => {
    console.log("err" + error); // for debug
    Toast.fail({
      message: error.message,
      duration: 1.5 * 1000
    });
    return Promise.reject(error);
  }
);

export default service;
