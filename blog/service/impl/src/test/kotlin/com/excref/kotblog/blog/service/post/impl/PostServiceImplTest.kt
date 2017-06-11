package com.excref.kotblog.blog.service.post.impl

import com.excref.kotblog.blog.persistence.post.PostRepository
import com.excref.kotblog.blog.service.blog.BlogService
import com.excref.kotblog.blog.service.category.CategoryService
import com.excref.kotblog.blog.service.post.PostService
import com.excref.kotblog.blog.service.tag.TagService
import com.excref.kotblog.blog.service.test.AbstractServiceImplTest
import org.assertj.core.api.Assertions.assertThat
import org.easymock.EasyMock
import org.easymock.Mock
import org.easymock.TestSubject
import org.junit.Test

/**
 * @author Rafayel Mirzoyan
 * @since 6/11/17 3:39 PM
 */
class PostServiceImplTest : AbstractServiceImplTest(){

    //region Test subject and mocks
    @TestSubject
    private val postService: PostService = PostServiceImpl()

    @Mock
    private lateinit var postRepository: PostRepository

    @Mock
    private lateinit var blogService: BlogService

    @Mock
    private lateinit var tagService: TagService

    @Mock
    private lateinit var categoryService: CategoryService
    //endregion

    //region initial
    @Test
    fun testPostService() {
        assertThat(postService).isNotNull()
    }

    @Test
    fun testPostRepository() {
        assertThat(postRepository).isNotNull()
    }
    //endregion

    //region create

    /**
     *  main scenario
     */

    @Test
    fun create2() {
        resetAll()
        val post = helper.buildPost()
        val tagUuids = post.tags.map { tg -> tg.uuid }.toList()
        val categoryUuids = post.categories.map { ctg -> ctg.uuid }.toList()
        // test data
        //expectations
        EasyMock.expect(blogService.getByUuid(post.blog.uuid)).andReturn(helper.buildBlog())
        EasyMock.expect(tagService.getByUuids(tagUuids)).andReturn(listOf(helper.buildTag()))
        EasyMock.expect(categoryService.getByUuids(categoryUuids)).andReturn(listOf(helper.buildCategory()))
//        expect(categoryRepository.save(isA(Category::class.java))).andAnswer({ EasyMock.getCurrentArguments()[0] as Category? })
        replayAll()
        // test scenario
        val category = postService.create(post.name, post.title, post.content, post.blog.uuid, tagUuids, categoryUuids)
        verifyAll()
    }
    //endregion

}
