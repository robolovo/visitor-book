# visitor-book

## Demo

<p align="center">
<img width="250" height="350" src="https://user-images.githubusercontent.com/55264231/205490068-7d462f0c-8ab5-4a86-b5ff-0b04d4b083d0.gif">
<img width="250" height="350" src="https://user-images.githubusercontent.com/55264231/205490157-4273d2e4-d4bf-4e79-9823-15e304841e22.gif">
</p>

<br>

## Summary

https://tech.kakao.com/2020/06/08/websocket-part1/ 의 아키텍처를 참고하여 간단하게 만들어본 실시간 방명록 어플리케이션이다.
<br>
아키텍처의 복잡도를 줄이기 위해 Nifi 대신 어플리케이션으로 대체하였고, Redis와 Kafka는 로컬에서 컨테이너로 기동하였다.
<br>

<p align="center">
<img width="450" height="350" src="https://user-images.githubusercontent.com/55264231/205491014-470f1bef-7f54-469d-a352-8fa40139e67f.png">
<img width="450" height="350" src="https://user-images.githubusercontent.com/55264231/205490997-2c78a17b-665c-46fe-abc2-4221bea4a42a.png">
</p>
