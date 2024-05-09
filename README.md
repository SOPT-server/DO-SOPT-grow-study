# Test 코드 스터디 정리


### Test Code를 작성하는 이유

#### Test Tool
- [JUnit5](https://junit.org/junit5/docs/current/user-guide/)
- [AssertJ](https://assertj.github.io/doc/)
- [Mockito](https://site.mockito.org/)

### 기본적인 Test 골격
- BDD 기반으로 시나리오를 정의하여 Test => given, when, then에 맞춰서 Test 작성함.
  
```Java

@Test
@DisplayName("")
void test() throws Exception {

}
```

#### Spring Boot에서의 Test
- Spring Boot Test에서는 기본적으로 `@SpringBootTest`를 사용하여 테스트를 작성함.


### Layered Architecture 정리

![스크린샷 2024-05-09 오후 4 16 48](https://github.com/DO-SOPT-SERVER/unan/assets/81692211/7ec3aabe-d761-40da-8ab1-f1156642d1da)


## Entity 

### Entity 구현

```java
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseTimeEntity {

    private static final int MAX_AGE = 100;
    private static final int MAX_LENGTH = 12;
    private static final short CURRENT_GENERATION = 34;
    private static final Pattern NAME_PATTERN = Pattern.compile("가-힣");

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
    private String name;
    private String nickname;
    private int age;
    private boolean isDeleted = false;

    @Embedded
    private SOPT sopt;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private final List<Post> posts = new ArrayList<>();

    @Builder
    private Member(String name,
                   String nickname,
                   int age,
                   SOPT sopt) {
        validateAge(age);
        validateName(name);
        validateNickname(nickname);
        this.name = name;
        this.nickname = nickname;
        this.age = age;
        this.sopt = sopt;
        this.isDeleted = false;
    }

    private void validateAge(final int age) {
        if (0 > age || age > MAX_AGE) {
            throw new MemberException("회원의 나이는 0세 이상 100세 이하입니다.");
        }
    }

    // SOPT는 한국인만 가입 가능함.
    private void validateName(final String name) {
        if (NAME_PATTERN.matcher(name).matches()) {
            throw new MemberException("유저의 이름은 한글만 가능합니다.");
        }

       if (name.length() > MAX_LENGTH) {
            throw new MemberException("유저의 이름은 12자를 넘을 수 없습니다.");
       }
    }

    private void validateNickname(final String nickname) {

        if (nickname.length() > 8) {
            throw new MemberException("유저의 닉네임은 8자를 넘길 수 없습니다.");
        }
    }

    public void remove() {

        this.isDeleted = true;
    }

    public void updateSOPT(SOPT sopt) {
        this.sopt = sopt;
    }
}
```


## Repository

### Repository Test

Repository Test는 `@DataJpaTest`를 사용하여 작성할 수 있습니다. `@SpringBootTest`는 Application Context를 로드하고, Spring Bean들을 모두 등록한 뒤에 Test를 하기 때문에 속도가 많이 느려집니다.

```java
@DataJpaTest
@ActiveProfiles("test")
public class PostJpaRepositoryTest {

    @Autowired
    PostJpaRepository postJpaRepository;

    @Autowired
    MemberJpaRepository memberJpaRepository;

    @Test
    @DisplayName("사용자 이름으로 작성한 게시글을 모두 조회할 수 있다.")
    void findAllByMemberNameIn() {
      // given
        Member member1 = createMember("오해영");
        Member member2 = createMember("또오해영");
        memberJpaRepository.save(member1);
        memberJpaRepository.save(member2);
        Post post1 = createPost("제목1", "내용1", member1);
        Post post2 = createPost("제목2", "내용2", member1);
        Post post3 = createPost("제목3", "내용3", member2);
        postJpaRepository.saveAll(List.of(post1, post2, post3));

        // when
        List<Post> findPosts = postJpaRepository.findAllByMemberNameIn(List.of("오해영", "또오해영"));

        // then
         Assertions.assertThat(findPosts)
                .extracting("title", "content")
                .containsExactlyInAnyOrder(
                        Assertions.tuple("제목1", "내용1"),
                        Assertions.tuple("제목2", "내용2"),
                        Assertions.tuple("제목3", "내용3")
                );
    }

    private Post createPost(String title, String content, Member member) {
         return Post.builder()
                .title(title)
                .content(content)
                .member(member)
                .build();
    }

    private Member createMember(String name) {
        SOPT sopt = SOPT.builder()
                .part(Part.SERVER)
                .build();
        return Member.builder()
                .age(99)
                .name(name)
                .sopt(sopt)
                .nickname("5hae0")
                .build();
    }


}
```

### Repository를 Mocking 하고 Test 하는 경우

Mocking은 실제 객체가 아닌 가짜 객체를 생성하여 Test를 작성하는 것 또는 그러한 기술을 의미합니다.
- 단위 Test 자체가 독립적이어햐 하는데, 대부분의 코드들은 의존성을 갖고 있기 때문에, Mocking 없이 단위 Test를 작성하게 되면 Test하기가 어려워진다.


MemberRetrieverTest
- Member를 조회해오는 test에서 MemberJpaRepository를 Mocking하여 Test를 작성

```Java
@SpringBootTest  
@ActiveProfiles("test")  
public class MemberRetrieverTest {  
  
    @Autowired  
    MemberRetriever memberRetriever;  
  
    @MockBean  
    MemberJpaRepository memberJpaRepository;  
  
    @Test  
    void test() throws Exception {  
        // given  
        BDDMockito.when(  
                memberJpaRepository.findById(1L)  
        ).thenReturn(  
                Optional.ofNullable(Member.builder()  
                        .age(99)  
                        .name("오해영")  
                        .nickname("5hae0")  
                        .build())  
        );  
        // when  
        Member findMember = memberRetriever.findById(1L);  
  
        // then  
        assertThat(findMember)  
                .extracting("age", "name", "nickname")  
                .containsExactly(99, "오해영", "5hae0");  
    }  
}
```



## Service Test
Service 계층의 Test는 기본적으로 아래와 같이 Mocking 하여 단위 Test를 작성할 수 있습니다.

```Java
@ExtendWith(MockitoExtension.class)
public class MemberServiceTest {

    @InjectMocks
    private MemberService memberService;

    @Mock
    private MemberRegister memberRegister;

    @Mock
    private MemberRetriever memberRetriever;

    @Mock
    private MemberUpdater memberUpdater;

    @Mock
    private MemberRemover memberRemover;


    @Test
    void getMemberById() {
        BDDMockito.given(memberRetriever.findById(1L)).willReturn(
                Member .builder()
                        .age(99)
                        .name("오해영")
                        .nickname("5hae0")
                        .build()
        );

        Assertions.assertThat(memberService.getMemberById(1L))
                .extracting("age", "name", "nickname")
                .containsExactly(99, "오해영", "5hae0");
    }
    
}

```

## Controller Test
- Controller 계층 Test를 할 때는 `@WebMvcTest`를 사용하여 Test 할 수 있습니다.
  - Service를 Mocking하고 Test

```Java
@WebMvcTest(controllers = MemberController.class)
public class MemberControllerTest extends ControllerTestManager {

    @MockBean
    private MemberService memberService;
    @Test
    @DisplayName("신규 회원을 등록한다.")
    void createMember() throws Exception {

        // given
        when(memberService.create(any(MemberCreateRequest.class)))
                .thenReturn("/api/member/1");

        MemberCreateRequest request = new MemberCreateRequest(
                        "오해영",
                        "5hae0",
                        28,
                        SOPT.builder()
                                .part(Part.DESIGN)
                                .build()
                );

      // when, then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/member")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.header().string("Location", "/api/member/1"))
        ;
    }

    @Test
    @DisplayName("특정 회원 정보를 조회한다.")
    void test() throws Exception {
      // given
        Member member = Member.builder()
                        .age(28)
                        .name("오해영")
                        .nickname("5hae0")
                        .sopt(
                                SOPT.builder()
                                        .part(Part.DESIGN)
                                        .build()
                        ).build();

        BDDMockito.given(memberService.getMemberById(1L))
                .willReturn(MemberGetResponse.of(member));

      // when, then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/member/1"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("name").value("오해영"))
                .andExpect(MockMvcResultMatchers.jsonPath("age").value(28))
                .andExpect(MockMvcResultMatchers.jsonPath("nickname").value("5hae0"))
                .andExpect(MockMvcResultMatchers.jsonPath("soptInfo.part").value("DESIGN"))
                .andExpect(MockMvcResultMatchers.jsonPath("soptInfo.generation").value(34));

    }

}
```



## 통합 테스트
- 각각의 계층별로 Test를 진행했지만, 모의 객체를 사용한 부분도 있고 각각의 계층에서는 문제가 없지만, 실제 동작할 때 문제가 생기는 상황들도 존재한다. 이를 줄이기 위해서 통합 테스트를 작성할 수 있다.

- Request -> Controller -> Service -> Repository -> Service -> Controller -> Response


Test Coverage 확보를 위해서 통합 테스트 또한 작성할 수 있습니다.