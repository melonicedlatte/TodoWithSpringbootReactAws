import { API_BASE_URL } from "../app-config";
const ACCESS_TOKEN = "ACCESS_TOKEN"

export function call(api, method, request) {
    let headers = new Headers({
        "Content-Type": "application/json",
    });

    const accessToken = localStorage.getItem(ACCESS_TOKEN);
    if (accessToken && accessToken !== null) {
        headers.append("Authorization", "Bearer " + accessToken);
    }

    let options = {
        headers: headers,
        url: API_BASE_URL + api,
        method: method,
    };

    if (request){
        options.body = JSON.stringify(request);
    }

    const result = fetch(options.url, options)
        .then((response) => {
            if (response.status === 200){
                return response.json();
            }else{
                return Promise.reject(response);
            }
        }

            // 아래는 비동기 문제로 안됨 
            //     if (response.status === 200){
            //         response.json().then((json) => { 
            //             return json;
            //         });
            //     }else{
            //         return Promise.reject(response);
            //     }
            // }

            // 원래 코드 : Spring Boot 버전이 상이하여 return 으로 json을 주지 않아 위와 같이 변경 (아래의 코드 동작 불가)
            // response.json().then((json) => { 
            //     if (!response.ok) {
            //         // response.ok가 true이면 정상적인 리스폰스를 받은것, 아니면 에러 리스폰스를 받은것.
            //         return Promise.reject(json);
            //     }
            //     return json;
            // }
            // )}
        ) 
        .catch((error) => {
            console.log("error : ", error);
            if (error.status === 403){
                window.location.href = "/login"; // 로그인 페이지로 이동
            }
            return Promise.reject(error);
        });

    return result;
}

export function signin(userDTO){
    return call("/auth/signin", "POST", userDTO)
        .then((response) => {
            if (response.token){
                // 로컬 스토리지에 토큰 저장
                localStorage.setItem(ACCESS_TOKEN, response.token);
                // token이 존재하는 경우 Todo 화면으로 리디렉트
                window.location.href = "/"; // token 존재 시에 메인 페이지로 이동
            }
            // console.log("response : ", response);
            // alert("로그인 token : " + response.token);
        });
}

export function signout() {
    localStorage.setItem(ACCESS_TOKEN, null);
    window.location.href = "/login";
}

export function signup(userDTO){
    return call("/auth/signup", "POST", userDTO);
}