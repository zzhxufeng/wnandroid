package name.zzhxufeng.wanandroid.viewmodels

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import kotlinx.coroutines.launch
import name.zzhxufeng.wanandroid.pagesource.ArticleSource
import name.zzhxufeng.wanandroid.repository.*
import name.zzhxufeng.wanandroid.screens.WanScreen

class MainViewModel : ViewModel() {

    val selectedScreen =  mutableStateOf<WanScreen>(WanScreen.Home)

    /*ui state*/
    val spinner = mutableStateOf(false)
    val snackBar = mutableStateOf<String?>(null)

    /*paging flow*/
    val articleFlow = Pager(
        config = PagingConfig(pageSize = 20),
        pagingSourceFactory = { ArticleSource() }
    ).flow.cachedIn(viewModelScope)

    /*data*/
    var articles = mutableStateOf(listOf<ArticleModel>())
        private set
    private var articlePageId = 0

    var banners = mutableStateOf(listOf<BannerModel>())
        private set

    val posts = mutableStateListOf<ArticleModel>()
    private var postsPageId = 0

    private fun refreshArticles() = launchDataLoad {
        articles.value = articles.value + ArticleRepository.refreshArticles(articlePageId)
        articlePageId ++
    }

    private fun refreshBanner() = launchDataLoad {
        banners.value = BannerRepository.refreshBanner()
    }

    private fun refreshPosts() = launchDataLoad {
        posts.addAll(PostsRepository.refreshPosts(postsPageId++))
    }

    init {
        /*refresh the main page*/
        refreshBanner()
        refreshArticles()
    }

    /**
     * Helper function to call a data load function with a loading spinner, errors will trigger a
     * snackbar.
     *
     * By marking `block` as `suspend` this creates a suspend lambda which can call suspend
     * functions.
     *
     * @param block lambda to actually load data. It is called in the viewModelScope. Before calling the
     *              lambda the loading spinner will display, after completion or error the loading
     *              spinner will stop
     */
    private fun launchDataLoad(block: suspend () -> Unit) {
        viewModelScope.launch {
            try {
                spinner.value = true
                block()
            } catch (error: ArticleRefreshError) {
                snackBar.value = error.message
            } finally {
                spinner.value = false
            }
        }
    }
}