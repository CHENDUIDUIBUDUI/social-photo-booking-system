package com.photo.booking.service.impl;

import com.photo.booking.entity.Content;
import com.photo.booking.mapper.ContentMapper;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ContentServiceImplTest {

    @Mock
    private ContentMapper contentMapper;

    @InjectMocks
    private ContentServiceImpl contentService;

    public ContentServiceImplTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateContent() {
        // 准备测试数据
        Content content = new Content();
        content.setTitle("Test Content");
        content.setDescription("Test Description");

        // 执行测试
        Content actualContent = contentService.createContent(content);

        // 验证结果
        assertEquals(content, actualContent);
        verify(contentMapper, times(1)).insert(content);
    }

    @Test
    void testGetContentById() {
        // 准备测试数据
        Long contentId = 1L;
        Content expectedContent = new Content();
        expectedContent.setId(contentId);
        expectedContent.setTitle("Test Content");

        // 模拟mapper行为
        when(contentMapper.selectById(contentId)).thenReturn(expectedContent);

        // 执行测试
        Content actualContent = contentService.getContentById(contentId);

        // 验证结果
        assertEquals(expectedContent, actualContent);
        verify(contentMapper, times(1)).selectById(contentId);
        verify(contentMapper, times(1)).updateViewCount(contentId);
    }

    @Test
    void testGetContentById_NotFound() {
        // 准备测试数据
        Long contentId = 1L;

        // 模拟mapper行为
        when(contentMapper.selectById(contentId)).thenReturn(null);

        // 执行测试
        Content actualContent = contentService.getContentById(contentId);

        // 验证结果
        assertNull(actualContent);
        verify(contentMapper, times(1)).selectById(contentId);
        verify(contentMapper, never()).updateViewCount(contentId);
    }

    @Test
    void testUpdateContent() {
        // 准备测试数据
        Content content = new Content();
        content.setId(1L);
        content.setTitle("Updated Content");

        // 执行测试
        Content actualContent = contentService.updateContent(content);

        // 验证结果
        assertEquals(content, actualContent);
        verify(contentMapper, times(1)).update(content);
    }

    @Test
    void testUpdateContentStatus() {
        // 准备测试数据
        Long contentId = 1L;
        Integer status = 1;

        // 执行测试
        contentService.updateContentStatus(contentId, status);

        // 验证结果
        verify(contentMapper, times(1)).updateStatus(contentId, status);
    }

    @Test
    void testGetContentList() {
        // 准备测试数据
        Integer type = 0;
        Integer status = 1;
        String tags = "test";
        String city = "Beijing";
        List<Content> expectedList = new ArrayList<>();
        Content content1 = new Content();
        content1.setId(1L);
        content1.setTitle("Content 1");
        expectedList.add(content1);

        // 模拟mapper行为
        when(contentMapper.selectList(type, status, tags, city, 10, 0)).thenReturn(expectedList);

        // 执行测试
        List<Content> actualList = contentService.getContentList(type, status, tags, city, 1, 10, 0);

        // 验证结果
        assertEquals(expectedList, actualList);
        verify(contentMapper, times(1)).selectList(type, status, tags, city, 10, 0);
    }

    @Test
    void testGetContentByUserId() {
        // 准备测试数据
        Long userId = 1L;
        Integer type = 0;
        List<Content> expectedList = new ArrayList<>();
        Content content1 = new Content();
        content1.setId(1L);
        content1.setTitle("Content 1");
        expectedList.add(content1);

        // 模拟mapper行为
        when(contentMapper.selectByUserId(userId, type)).thenReturn(expectedList);

        // 执行测试
        List<Content> actualList = contentService.getContentByUserId(userId, type);

        // 验证结果
        assertEquals(expectedList, actualList);
        verify(contentMapper, times(1)).selectByUserId(userId, type);
    }

    @Test
    void testGetPendingReview() {
        // 准备测试数据
        List<Content> expectedList = new ArrayList<>();
        Content content1 = new Content();
        content1.setId(1L);
        content1.setTitle("Content 1");
        expectedList.add(content1);

        // 模拟mapper行为
        when(contentMapper.selectPendingReview()).thenReturn(expectedList);

        // 执行测试
        List<Content> actualList = contentService.getPendingReview();

        // 验证结果
        assertEquals(expectedList, actualList);
        verify(contentMapper, times(1)).selectPendingReview();
    }
}
