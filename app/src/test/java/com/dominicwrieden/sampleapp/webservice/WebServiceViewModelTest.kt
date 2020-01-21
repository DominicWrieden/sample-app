package com.dominicwrieden.sampleapp.webservice

import com.InstantTaskExecutorRule
import com.RxSchedulerRule
import com.dominicwrieden.sampleapp.data.entity.Post
import com.dominicwrieden.sampleapp.data.repository.PostRepository
import com.dominicwrieden.testObserver
import com.google.common.truth.Truth
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.Observable
import io.reactivex.Single
import okhttp3.Protocol
import okhttp3.Request
import okhttp3.Response
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import java.net.UnknownHostException
import javax.net.ssl.SSLHandshakeException

class WebServiceViewModelTest {
    @get:Rule
    val taskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val rxSchedulerRule = RxSchedulerRule()


    @Mock
    lateinit var postRepository: PostRepository


    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
    }


    @Test
    fun initial() {
        val reponse = Response.Builder()
            .request(Request.Builder().url("https://127.0.0.1").build())
            .protocol(Protocol.HTTP_2).code(200).message("asda").build()

        whenever(postRepository.getPosts()).doReturn(Observable.just(emptyList()))
        whenever(postRepository.refreshPosts()).doReturn(Single.just(reponse))

        val webServiceViewModel = WebServiceViewModel(postRepository)
        val postListeState = webServiceViewModel.postListeState.testObserver()
        val loadingState = webServiceViewModel.getLoadingStateTest().testObserver()
        val errorState = webServiceViewModel.updateErrorState.testObserver()

        Truth.assert_()
            .that(postListeState.observedValues.last())
            .isEqualTo(PostListState.EmptyList)

        Truth.assert_()
            .that(loadingState.observedValues.count { it is LoadingState.Loading })
            .isEqualTo(1)

        Truth.assert_()
            .that(loadingState.observedValues.count { it is LoadingState.NotLoading })
            .isEqualTo(1)

        Truth.assert_()
            .that(errorState.observedValues)
            .isEmpty()
    }


    @Test
    fun empty_list() {
        val reponse = Response.Builder()
            .request(Request.Builder().url("https://127.0.0.1").build())
            .protocol(Protocol.HTTP_2).code(200).message("asda").build()

        whenever(postRepository.getPosts()).doReturn(Observable.just(emptyList()))
        whenever(postRepository.refreshPosts()).doReturn(Single.just(reponse))

        val webServiceViewModel = WebServiceViewModel(postRepository)

        val postListeState = webServiceViewModel.postListeState.testObserver()
        val loadingState = webServiceViewModel.getLoadingStateTest().testObserver()
        val errorState = webServiceViewModel.updateErrorState.testObserver()

        Truth.assert_()
            .that(postListeState.observedValues.last())
            .isEqualTo(PostListState.EmptyList)

        Truth.assert_()
            .that(loadingState.observedValues.last())
            .isEqualTo(LoadingState.NotLoading)


        Truth.assert_()
            .that(errorState.observedValues)
            .isEmpty()
    }

    @Test
    fun element_1() {
        val posts = listOf(Post(1, 1, "Title", "Body"))
        val reponse = Response.Builder()
            .request(Request.Builder().url("https://127.0.0.1").build())
            .protocol(Protocol.HTTP_2).code(200).message("asda").build()

        whenever(postRepository.getPosts()).doReturn(Observable.just(posts))
        whenever(postRepository.refreshPosts()).doReturn(Single.just(reponse))

        val webServiceViewModel = WebServiceViewModel(postRepository)

        val postListeState = webServiceViewModel.postListeState.testObserver()
        val loadingState = webServiceViewModel.getLoadingStateTest().testObserver()
        val errorState = webServiceViewModel.updateErrorState.testObserver()

        Truth.assert_()
            .that(postListeState.observedValues.last())
            .isEqualTo(PostListState.PostList(posts))

        Truth.assert_()
            .that(loadingState.observedValues.last())
            .isEqualTo(LoadingState.NotLoading)


        Truth.assert_()
            .that(errorState.observedValues)
            .isEmpty()
    }

    @Test
    fun element_3() {
        val posts = listOf(
            Post(1, 1, "Title1", "Body1"),
            Post(2, 1, "Title2", "Body2"),
            Post(3, 2, "Title3", "Body3")
        )
        val reponse = Response.Builder()
            .request(Request.Builder().url("https://127.0.0.1").build())
            .protocol(Protocol.HTTP_2).code(200).message("asda").build()

        whenever(postRepository.getPosts()).doReturn(Observable.just(posts))
        whenever(postRepository.refreshPosts()).doReturn(Single.just(reponse))

        val webServiceViewModel = WebServiceViewModel(postRepository)

        val postListeState = webServiceViewModel.postListeState.testObserver()
        val loadingState = webServiceViewModel.getLoadingStateTest().testObserver()
        val errorState = webServiceViewModel.updateErrorState.testObserver()

        Truth.assert_()
            .that(postListeState.observedValues.last())
            .isEqualTo(PostListState.PostList(posts))

        Truth.assert_()
            .that(loadingState.observedValues.last())
            .isEqualTo(LoadingState.NotLoading)


        Truth.assert_()
            .that(errorState.observedValues)
            .isEmpty()
    }


    @Test
    fun element_3_load_new() {
        val posts = listOf(
            Post(1, 1, "Title1", "Body1"),
            Post(2, 1, "Title2", "Body2"),
            Post(3, 2, "Title3", "Body3")
        )
        val reponse = Response.Builder()
            .request(Request.Builder().url("https://127.0.0.1").build())
            .protocol(Protocol.HTTP_2).code(200).message("asda").build()

        whenever(postRepository.getPosts()).doReturn(Observable.just(posts))

        whenever(postRepository.refreshPosts()).doReturn(Single.just(reponse))

        val webServiceViewModel = WebServiceViewModel(postRepository)

        val postListeState = webServiceViewModel.postListeState.testObserver()
        val loadingState = webServiceViewModel.getLoadingStateTest().testObserver()
        val errorState = webServiceViewModel.updateErrorState.testObserver()

        webServiceViewModel.refreshTriggered()

        Truth.assert_()
            .that(postListeState.observedValues.last())
            .isEqualTo(PostListState.PostList(posts))

        Truth.assert_()
            .that(loadingState.observedValues.count { it is LoadingState.Loading })
            .isEqualTo(2)


        Truth.assert_()
            .that(errorState.observedValues)
            .isEmpty()
    }


    @Test
    fun element_1_missing_title() {
        val posts = listOf(Post(1, 1, "", "Body"))
        val reponse = Response.Builder()
            .request(Request.Builder().url("https://127.0.0.1").build())
            .protocol(Protocol.HTTP_2).code(200).message("asda").build()

        whenever(postRepository.getPosts()).doReturn(Observable.just(posts))
        whenever(postRepository.refreshPosts()).doReturn(Single.just(reponse))

        val webServiceViewModel = WebServiceViewModel(postRepository)

        val postListeState = webServiceViewModel.postListeState.testObserver()
        val loadingState = webServiceViewModel.getLoadingStateTest().testObserver()
        val errorState = webServiceViewModel.updateErrorState.testObserver()

        Truth.assert_()
            .that(postListeState.observedValues.last())
            .isEqualTo(PostListState.PostList(posts))

        Truth.assert_()
            .that(loadingState.observedValues.last())
            .isEqualTo(LoadingState.NotLoading)


        Truth.assert_()
            .that(errorState.observedValues)
            .isEmpty()
    }

    @Test
    fun element_1_missing_body() {
        val posts = listOf(Post(1, 1, "Title", ""))
        val reponse = Response.Builder()
            .request(Request.Builder().url("https://127.0.0.1").build())
            .protocol(Protocol.HTTP_2).code(200).message("asda").build()

        whenever(postRepository.getPosts()).doReturn(Observable.just(posts))
        whenever(postRepository.refreshPosts()).doReturn(Single.just(reponse))

        val webServiceViewModel = WebServiceViewModel(postRepository)

        val postListeState = webServiceViewModel.postListeState.testObserver()
        val loadingState = webServiceViewModel.getLoadingStateTest().testObserver()
        val errorState = webServiceViewModel.updateErrorState.testObserver()

        Truth.assert_()
            .that(postListeState.observedValues.last())
            .isEqualTo(PostListState.PostList(posts))

        Truth.assert_()
            .that(loadingState.observedValues.last())
            .isEqualTo(LoadingState.NotLoading)


        Truth.assert_()
            .that(errorState.observedValues)
            .isEmpty()
    }

    @Test
    fun element_1_missing_title_body() {
        val posts = listOf(Post(1, 1, "", ""))
        val reponse = Response.Builder()
            .request(Request.Builder().url("https://127.0.0.1").build())
            .protocol(Protocol.HTTP_2).code(200).message("asda").build()

        whenever(postRepository.getPosts()).doReturn(Observable.just(posts))
        whenever(postRepository.refreshPosts()).doReturn(Single.just(reponse))

        val webServiceViewModel = WebServiceViewModel(postRepository)

        val postListeState = webServiceViewModel.postListeState.testObserver()
        val loadingState = webServiceViewModel.getLoadingStateTest().testObserver()
        val errorState = webServiceViewModel.updateErrorState.testObserver()

        Truth.assert_()
            .that(postListeState.observedValues.last())
            .isEqualTo(PostListState.PostList(posts))

        Truth.assert_()
            .that(loadingState.observedValues.last())
            .isEqualTo(LoadingState.NotLoading)


        Truth.assert_()
            .that(errorState.observedValues)
            .isEmpty()
    }

    @Test
    fun initial_loading_error_no_internet_connection() {
        whenever(postRepository.getPosts()).doReturn(Observable.just(emptyList()))
        whenever(postRepository.refreshPosts())
            .doReturn(Single.error(UnknownHostException()))

        val webServiceViewModel = WebServiceViewModel(postRepository)

        val postListeState = webServiceViewModel.postListeState.testObserver()
        val loadingState = webServiceViewModel.getLoadingStateTest().testObserver()
        val errorState = webServiceViewModel.updateErrorState.testObserver()

        Truth.assert_()
            .that(postListeState.observedValues.last())
            .isEqualTo(PostListState.EmptyList)

        Truth.assert_()
            .that(loadingState.observedValues.last())
            .isEqualTo(LoadingState.NotLoading)


        Truth.assert_()
            .that(errorState.observedValues.last())
            .isEqualTo(UpdateErrorState.NoInternetUpdateError)
    }

    @Test
    fun initial_loading_error_other() {
        whenever(postRepository.getPosts()).doReturn(Observable.just(emptyList()))
        whenever(postRepository.refreshPosts()).doReturn(Single.error(Exception()))

        val webServiceViewModel = WebServiceViewModel(postRepository)

        val postListeState = webServiceViewModel.postListeState.testObserver()
        val loadingState = webServiceViewModel.getLoadingStateTest().testObserver()
        val errorState = webServiceViewModel.updateErrorState.testObserver()

        Truth.assert_()
            .that(postListeState.observedValues.last())
            .isEqualTo(PostListState.EmptyList)

        Truth.assert_()
            .that(loadingState.observedValues.last())
            .isEqualTo(LoadingState.NotLoading)


        Truth.assert_()
            .that(errorState.observedValues.last())
            .isEqualTo(UpdateErrorState.OtherUpdateError)
    }

    @Test
    fun element_3_loading_error_no_connection() {
        val posts = listOf(
            Post(1, 1, "Title1", "Body1"),
            Post(2, 1, "Title2", "Body2"),
            Post(3, 2, "Title3", "Body3")
        )

        whenever(postRepository.getPosts()).doReturn(Observable.just(posts))
        whenever(postRepository.refreshPosts())
            .doReturn(Single.error(UnknownHostException()))

        val webServiceViewModel = WebServiceViewModel(postRepository)

        val postListeState = webServiceViewModel.postListeState.testObserver()
        val loadingState = webServiceViewModel.getLoadingStateTest().testObserver()
        val errorState = webServiceViewModel.updateErrorState.testObserver()

        Truth.assert_()
            .that(postListeState.observedValues.last())
            .isEqualTo(PostListState.PostList(posts))

        Truth.assert_()
            .that(loadingState.observedValues.last())
            .isEqualTo(LoadingState.NotLoading)


        Truth.assert_()
            .that(errorState.observedValues.last())
            .isEqualTo(UpdateErrorState.NoInternetUpdateError)
    }

    @Test
    fun element_3_loading_error_other() {
        val posts = listOf(
            Post(1, 1, "Title1", "Body1"),
            Post(2, 1, "Title2", "Body2"),
            Post(3, 2, "Title3", "Body3")
        )

        whenever(postRepository.getPosts()).doReturn(Observable.just(posts))
        whenever(postRepository.refreshPosts()).doReturn(Single.error(Exception()))

        val webServiceViewModel = WebServiceViewModel(postRepository)

        val postListeState = webServiceViewModel.postListeState.testObserver()
        val loadingState = webServiceViewModel.getLoadingStateTest().testObserver()
        val errorState = webServiceViewModel.updateErrorState.testObserver()

        Truth.assert_()
            .that(postListeState.observedValues.last())
            .isEqualTo(PostListState.PostList(posts))

        Truth.assert_()
            .that(loadingState.observedValues.last())
            .isEqualTo(LoadingState.NotLoading)


        Truth.assert_()
            .that(errorState.observedValues.last())
            .isEqualTo(UpdateErrorState.OtherUpdateError)
    }

    /**
     * For old Android-Versions < 4.4
     */
    @Test
    fun error_ssl_handshake_exception() {
        val posts = listOf(
            Post(1, 1, "Title1", "Body1"),
            Post(2, 1, "Title2", "Body2"),
            Post(3, 2, "Title3", "Body3")
        )

        whenever(postRepository.getPosts()).doReturn(Observable.just(posts))
        whenever(postRepository.refreshPosts())
            .doReturn(Single.error(SSLHandshakeException("SSLv3 protocol are being used")))

        val webServiceViewModel = WebServiceViewModel(postRepository)

        val postListeState = webServiceViewModel.postListeState.testObserver()
        val loadingState = webServiceViewModel.getLoadingStateTest().testObserver()
        val errorState = webServiceViewModel.updateErrorState.testObserver()

        Truth.assert_()
            .that(postListeState.observedValues.last())
            .isEqualTo(PostListState.PostList(posts))

        Truth.assert_()
            .that(loadingState.observedValues.last())
            .isEqualTo(LoadingState.NotLoading)


        Truth.assert_()
            .that(errorState.observedValues.last())
            .isEqualTo(UpdateErrorState.SSLv3UpdateError)
    }

}