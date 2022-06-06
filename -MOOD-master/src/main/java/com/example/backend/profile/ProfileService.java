package com.example.backend.profile;

import com.example.backend.diary.Diary.*;
import com.example.backend.member.MemberAuthDao;
import com.example.backend.member.UserAuth;
import com.example.backend.music.MusicAdapater;
import com.example.backend.music.MusicDto;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
@Slf4j
@AllArgsConstructor
@Service

public class ProfileService {
    DiaryDao diaryDao;
    MemberAuthDao memberAuthDao;
    DiaryMusicDao diaryMusicDao;
    DiaryAnalyticsDao diaryAnalyticsDao;
    DiaryAnalyticsSentimentDao diaryAnalyticsSentimentDao;
    public List<MusicDto> getMyPlayList(String id){
        Sort sort =sortByDate();
        List<MusicDto> result=new ArrayList<MusicDto>();
        UserAuth userAuth= MemberAuthDao.getUserAuthByUid(id);
        List<Diary> tmp_DiaryList=diaryDao.getDiaryByUserAuthAndIsDeletedIsFalse(userAuth,sort);
        for(int i=0;i<tmp_DiaryList.size();i++) {
            MusicDto musicDto=new MusicDto();
            DiaryMusic currentDiaryMusic=diaryMusicDao.getDiaryMusicByDiary(tmp_DiaryList.get(i));
            musicDto=MusicAdapater.entityToDto(tmp_DiaryList.get(i), currentDiaryMusic.getMusicInfo());
            result.add(musicDto);
        }
        return result;
    }

    public List<DiaryDto> getMyDiary(String id){
        Sort sort = sortByDate();
        List<DiaryDto> result =new ArrayList<DiaryDto>();
        UserAuth userAuth= MemberAuthDao.getUserAuthByUid(id);
        List<Diary> tmp_DiaryList=diaryDao.getDiaryByUserAuthAndIsDeletedIsFalse(userAuth,sort);
        for(int i=0;i<tmp_DiaryList.size();i++) {
            DiaryDto tmpDiaryDto=new DiaryDto();
            DiaryAnalytics diaryAnalytics = diaryAnalyticsDao.getDiaryAnalyticsByDiary(tmp_DiaryList.get(i));
            DiaryAnalyticsSentiment diaryAnalyticsSentiment=diaryAnalyticsSentimentDao.getDiaryAnalyticsSentimentByDiary(tmp_DiaryList.get(i));
            DiaryMusic diaryMusic=diaryMusicDao.getDiaryMusicByDiary(tmp_DiaryList.get(i));
            DiaryAdapter DiaryAdapater = null;
            tmpDiaryDto=DiaryAdapater.entityToDto(diaryAnalytics, diaryAnalyticsSentiment, tmp_DiaryList.get(i), diaryMusic);
            result.add(tmpDiaryDto);
        }

        return result;
    }
    public int getMYReportDiary(String id,LocalDate startDate,LocalDate endDate) {
        UserAuth userAuth= MemberAuthDao.getUserAuthByUid(id);
        List<Diary>  tmpDiaryList=diaryDao.findDiaryByUserAuthAndIsDeletedIsFalseAndDiaryDateBetween(userAuth, startDate, endDate);
        return tmpDiaryList.size();
    }

    private static Sort sortByDate() {
        return Sort.by(Sort.Direction.DESC,"diaryDate");
    }

}
