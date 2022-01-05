# 오준석의 안드로이드 생존코딩: 코틀린 편 개정판
예제 소스 코드

examples 폴더에 각 예제별 소스 코드가 있습니다.

# 오류
- 419페이지 : 소스 코드 중에 // 클릭시 처리 부분에 코드 누락되어 다음 두 줄 추가
```
// 클릭시 처리
viewModel.selectedTodo = todo
findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
```
