package com.wangtiantian.entity.koubei;

import java.util.Objects;

public class ReplyKouBei {
    private int C_ID;public void set_C_ID(int C_ID){this.C_ID =C_ID;}public int get_C_ID(){return C_ID;}
    private String C_KouBeiID;public void set_C_KouBeiID(String C_KouBeiID){this.C_KouBeiID = C_KouBeiID;} public String get_C_KouBeiID(){return C_KouBeiID;}
    private String  C_ReplyContent; public void set_C_ReplyContent(String C_ReplyContent){this.C_ReplyContent=C_ReplyContent.replace("\n","").replace("\r","").replace("\t","").replace("'","''").trim();}public String get_C_ReplyContent(){return C_ReplyContent;}
    private String  C_ReplyUserID; public void set_C_ReplyUserID(String C_ReplyUserID){this.C_ReplyUserID=C_ReplyUserID.replace("\n","").replace("\r","").replace("\t","").replace("'","''").trim();}public String get_C_ReplyUserID(){return C_ReplyUserID;}
    private String  C_ReplyUserName; public void set_C_ReplyUserName(String C_ReplyUserName){this.C_ReplyUserName=C_ReplyUserName.replace("\n","").replace("\r","").replace("\t","").replace("'","''").trim();}public String get_C_ReplyUserName(){return C_ReplyUserName;}
    private int  C_IsFinish;public int  get_C_IsFinish(){return C_IsFinish;}public void set_C_IsFinish(int C_IsFinish){this.C_IsFinish=C_IsFinish;}

    private String  C_replydate; public void set_C_replydate(String C_replydate){this.C_replydate=C_replydate.replace("\n","").replace("\r","").replace("\t","").replace("'","''").trim();}public String get_C_replydate(){return C_replydate;}
    private String  C_chatcount; public void set_C_chatcount(String C_chatcount){this.C_chatcount=C_chatcount.replace("\n","").replace("\r","").replace("\t","").replace("'","''").trim();}public String get_C_chatcount(){return C_chatcount;}
    private String  C_iscarowner; public void set_C_iscarowner(String C_iscarowner){this.C_iscarowner=C_iscarowner.replace("\n","").replace("\r","").replace("\t","").replace("'","''").trim();}public String get_C_iscarowner(){return C_iscarowner;}
    private String  C_carownerlevels; public void set_C_carownerlevels(String C_carownerlevels){this.C_carownerlevels=C_carownerlevels.replace("\n","").replace("\r","").replace("\t","").replace("'","''").trim();}public String get_C_carownerlevels(){return C_carownerlevels;}
    private String  C_carname; public void set_C_carname(String C_carname){this.C_carname=C_carname.replace("\n","").replace("\r","").replace("\t","").replace("'","''").trim();}public String get_C_carname(){return C_carname;}
    private String  C_location; public void set_C_location(String C_location){this.C_location=C_location.replace("\n","").replace("\r","").replace("\t","").replace("'","''").trim();}public String get_C_location(){return C_location;}
    private String  C_forbidReply; public void set_C_forbidReply(String C_forbidReply){this.C_forbidReply=C_forbidReply.replace("\n","").replace("\r","").replace("\t","").replace("'","''").trim();}public String get_C_forbidReply(){return C_forbidReply;}
    private String  C_freplyCount; public void set_C_freplyCount(String C_freplyCount){this.C_freplyCount=C_freplyCount.replace("\n","").replace("\r","").replace("\t","").replace("'","''").trim();}public String get_C_freplyCount(){return C_freplyCount;}
    private String  C_rmemberSex; public void set_C_rmemberSex(String C_rmemberSex){this.C_rmemberSex=C_rmemberSex.replace("\n","").replace("\r","").replace("\t","").replace("'","''").trim();}public String get_C_rmemberSex(){return C_rmemberSex;}
    private String  C_robjId; public void set_C_robjId(String C_robjId){this.C_robjId=C_robjId.replace("\n","").replace("\r","").replace("\t","").replace("'","''").trim();}public String get_C_robjId(){return C_robjId;}
    private String  C_TargetUserID; public void set_C_TargetUserID(String C_TargetUserID){this.C_TargetUserID=C_TargetUserID.replace("\n","").replace("\r","").replace("\t","").replace("'","''").trim();}public String get_C_TargetUserID(){return C_TargetUserID;}

    private String  C_rreplyDate; public void set_C_rreplyDate(String C_rreplyDate){this.C_rreplyDate=C_rreplyDate.replace("\n","").replace("\r","").replace("\t","").replace("'","''").trim();}public String get_C_rreplyDate(){return C_rreplyDate;}
    private String  C_rup; public void set_C_rup(String C_rup){this.C_rup=C_rup.replace("\n","").replace("\r","").replace("\t","").replace("'","''").trim();}public String get_C_rup(){return C_rup;}
    private String  C_freplyId; public void set_C_freplyId(String C_freplyId){this.C_freplyId=C_freplyId.replace("\n","").replace("\r","").replace("\t","").replace("'","''").trim();}public String get_C_freplyId(){return C_freplyId;}
    private String  C_rtargetReplyId; public void set_C_rtargetReplyId(String C_rtargetReplyId){this.C_rtargetReplyId=C_rtargetReplyId.replace("\n","").replace("\r","").replace("\t","").replace("'","''").trim();}public String get_C_rtargetReplyId(){return C_rtargetReplyId;}
    private String  C_rtargetMemberId; public void set_C_rtargetMemberId(String C_rtargetMemberId){this.C_rtargetMemberId=C_rtargetMemberId.replace("\n","").replace("\r","").replace("\t","").replace("'","''").trim();}public String get_C_rtargetMemberId(){return C_rtargetMemberId;}
    private String  C_rfloor; public void set_C_rfloor(String C_rfloor){this.C_rfloor=C_rfloor.replace("\n","").replace("\r","").replace("\t","").replace("'","''").trim();}public String get_C_rfloor(){return C_rfloor;}
    private String  C_rcontentLength; public void set_C_rcontentLength(String C_rcontentLength){this.C_rcontentLength=C_rcontentLength.replace("\n","").replace("\r","").replace("\t","").replace("'","''").trim();}public String get_C_rcontentLength(){return C_rcontentLength;}
    private String  C_createType; public void set_C_createType(String C_createType){this.C_createType=C_createType.replace("\n","").replace("\r","").replace("\t","").replace("'","''").trim();}public String get_C_createType(){return C_createType;}
    private String  C_chatIndex; public void set_C_chatIndex(String C_chatIndex){this.C_chatIndex=C_chatIndex.replace("\n","").replace("\r","").replace("\t","").replace("'","''").trim();}public String get_C_chatIndex(){return C_chatIndex;}
    private String  C_ruserHeaderImage; public void set_C_ruserHeaderImage(String C_ruserHeaderImage){this.C_ruserHeaderImage=C_ruserHeaderImage.replace("\n","").replace("\r","").replace("\t","").replace("'","''").trim();}public String get_C_ruserHeaderImage(){return C_ruserHeaderImage;}
    private String  C_rcontent; public void set_C_rcontent(String C_rcontent){this.C_rcontent=C_rcontent.replace("\n","").replace("\r","").replace("\t","").replace("'","''").trim();}public String get_C_rcontent(){return C_rcontent;}
    private String  C_rmemberId; public void set_C_rmemberId(String C_rmemberId){this.C_rmemberId=C_rmemberId.replace("\n","").replace("\r","").replace("\t","").replace("'","''").trim();}public String get_C_rmemberId(){return C_rmemberId;}
    private String  C_rmemberName; public void set_C_rmemberName(String C_rmemberName){this.C_rmemberName=C_rmemberName.replace("\n","").replace("\r","").replace("\t","").replace("'","''").trim();}public String get_C_rmemberName(){return C_rmemberName;}
    private String  C_replyId; public void set_C_replyId(String C_replyId){this.C_replyId=C_replyId.replace("\n","").replace("\r","").replace("\t","").replace("'","''").trim();}public String get_C_replyId(){return C_replyId;}
    private String  C_replydateSub; public void set_C_replydateSub(String C_replydateSub){this.C_replydateSub=C_replydateSub.replace("\n","").replace("\r","").replace("\t","").replace("'","''").trim();}public String get_C_replydateSub(){return C_replydateSub;}
    private String  C_chatcountSub; public void set_C_chatcountSub(String C_chatcountSub){this.C_chatcountSub=C_chatcountSub.replace("\n","").replace("\r","").replace("\t","").replace("'","''").trim();}public String get_C_chatcountSub(){return C_chatcountSub;}
    private String  C_iscarownerSub; public void set_C_iscarownerSub(String C_iscarownerSub){this.C_iscarownerSub=C_iscarownerSub.replace("\n","").replace("\r","").replace("\t","").replace("'","''").trim();}public String get_C_iscarownerSub(){return C_iscarownerSub;}
    private String  C_carownerlevelsSub; public void set_C_carownerlevelsSub(String C_carownerlevelsSub){this.C_carownerlevelsSub=C_carownerlevelsSub.replace("\n","").replace("\r","").replace("\t","").replace("'","''").trim();}public String get_C_carownerlevelsSub(){return C_carownerlevelsSub;}
    private String  C_carnameSub; public void set_C_carnameSub(String C_carnameSub){this.C_carnameSub=C_carnameSub.replace("\n","").replace("\r","").replace("\t","").replace("'","''").trim();}public String get_C_carnameSub(){return C_carnameSub;}
    private String  C_forbidReplySub; public void set_C_forbidReplySub(String C_forbidReplySub){this.C_forbidReplySub=C_forbidReplySub.replace("\n","").replace("\r","").replace("\t","").replace("'","''").trim();}public String get_C_forbidReplySub(){return C_forbidReplySub;}
    private String  C_rmemberSexSub; public void set_C_rmemberSexSub(String C_rmemberSexSub){this.C_rmemberSexSub=C_rmemberSexSub.replace("\n","").replace("\r","").replace("\t","").replace("'","''").trim();}public String get_C_rmemberSexSub(){return C_rmemberSexSub;}
    private String  C_robjIdSub; public void set_C_robjIdSub(String C_robjIdSub){this.C_robjIdSub=C_robjIdSub.replace("\n","").replace("\r","").replace("\t","").replace("'","''").trim();}public String get_C_robjIdSub(){return C_robjIdSub;}
    private String  C_rreplyDateSub; public void set_C_rreplyDateSub(String C_rreplyDateSub){this.C_rreplyDateSub=C_rreplyDateSub.replace("\n","").replace("\r","").replace("\t","").replace("'","''").trim();}public String get_C_rreplyDateSub(){return C_rreplyDateSub;}
    private String  C_rupSub; public void set_C_rupSub(String C_rupSub){this.C_rupSub=C_rupSub.replace("\n","").replace("\r","").replace("\t","").replace("'","''").trim();}public String get_C_rupSub(){return C_rupSub;}
    private String  C_replyIdSub; public void set_C_replyIdSub(String C_replyIdSub){this.C_replyIdSub=C_replyIdSub.replace("\n","").replace("\r","").replace("\t","").replace("'","''").trim();}public String get_C_replyIdSub(){return C_replyIdSub;}
    private String  C_freplyIdSub; public void set_C_freplyIdSub(String C_freplyIdSub){this.C_freplyIdSub=C_freplyIdSub.replace("\n","").replace("\r","").replace("\t","").replace("'","''").trim();}public String get_C_freplyIdSub(){return C_freplyIdSub;}
    private String  C_rtargetReplyIdSub; public void set_C_rtargetReplyIdSub(String C_rtargetReplyIdSub){this.C_rtargetReplyIdSub=C_rtargetReplyIdSub.replace("\n","").replace("\r","").replace("\t","").replace("'","''").trim();}public String get_C_rtargetReplyIdSub(){return C_rtargetReplyIdSub;}
    private String  C_rtargetMemberIdSub; public void set_C_rtargetMemberIdSub(String C_rtargetMemberIdSub){this.C_rtargetMemberIdSub=C_rtargetMemberIdSub.replace("\n","").replace("\r","").replace("\t","").replace("'","''").trim();}public String get_C_rtargetMemberIdSub(){return C_rtargetMemberIdSub;}
    private String  C_rfloorSub; public void set_C_rfloorSub(String C_rfloorSub){this.C_rfloorSub=C_rfloorSub.replace("\n","").replace("\r","").replace("\t","").replace("'","''").trim();}public String get_C_rfloorSub(){return C_rfloorSub;}
    private String  C_rcontentLengthSub; public void set_C_rcontentLengthSub(String C_rcontentLengthSub){this.C_rcontentLengthSub=C_rcontentLengthSub.replace("\n","").replace("\r","").replace("\t","").replace("'","''").trim();}public String get_C_rcontentLengthSub(){return C_rcontentLengthSub;}
    private String  C_createTypeSub; public void set_C_createTypeSub(String C_createTypeSub){this.C_createTypeSub=C_createTypeSub.replace("\n","").replace("\r","").replace("\t","").replace("'","''").trim();}public String get_C_createTypeSub(){return C_createTypeSub;}
    private String  C_chatIndexSub; public void set_C_chatIndexSub(String C_chatIndexSub){this.C_chatIndexSub=C_chatIndexSub.replace("\n","").replace("\r","").replace("\t","").replace("'","''").trim();}public String get_C_chatIndexSub(){return C_chatIndexSub;}
    private String  C_freplyCountSub; public void set_C_freplyCountSub(String C_freplyCountSub){this.C_freplyCountSub=C_freplyCountSub.replace("\n","").replace("\r","").replace("\t","").replace("'","''").trim();}public String get_C_freplyCountSub(){return C_freplyCountSub;}
    private String  C_rcontentSub; public void set_C_rcontentSub(String C_rcontentSub){this.C_rcontentSub=C_rcontentSub.replace("\n","").replace("\r","").replace("\t","").replace("'","''").trim();}public String get_C_rcontentSub(){return C_rcontentSub;}
    private String  C_rmemberIdSub; public void set_C_rmemberIdSub(String C_rmemberIdSub){this.C_rmemberIdSub=C_rmemberIdSub.replace("\n","").replace("\r","").replace("\t","").replace("'","''").trim();}public String get_C_rmemberIdSub(){return C_rmemberIdSub;}
    private String  C_rmemberNameSub; public void set_C_rmemberNameSub(String C_rmemberNameSub){this.C_rmemberNameSub=C_rmemberNameSub.replace("\n","").replace("\r","").replace("\t","").replace("'","''").trim();}public String get_C_rmemberNameSub(){return C_rmemberNameSub;}
    private String  C_ruserHeaderImageSub; public void set_C_ruserHeaderImageSub(String C_ruserHeaderImageSub){this.C_ruserHeaderImageSub=C_ruserHeaderImageSub.replace("\n","").replace("\r","").replace("\t","").replace("'","''").trim();}public String get_C_ruserHeaderImageSub(){return C_ruserHeaderImageSub;}
    private String  C_badge_icon; public void set_C_badge_icon(String C_badge_icon){this.C_badge_icon=C_badge_icon.replace("\n","").replace("\r","").replace("\t","").replace("'","''").trim();}public String get_C_badge_icon(){return C_badge_icon;}
    private String C_badge_user_id; public void set_C_badge_user_id(String C_badge_user_id){this.C_badge_user_id =C_badge_user_id.replace("\n","").replace("\r","").replace("\t","").replace("'","''").trim();}public String get_C_badge_user_id(){return C_badge_user_id;}
    private String  C_badge_achievement_id; public void set_C_badge_achievement_id(String C_badge_achievement_id){this.C_badge_achievement_id=C_badge_achievement_id.replace("\n","").replace("\r","").replace("\t","").replace("'","''").trim();}public String get_C_badge_achievement_id(){return C_badge_achievement_id;}
    private String  C_badge_name; public void set_C_badge_name(String C_badge_name){this.C_badge_name=C_badge_name.replace("\n","").replace("\r","").replace("\t","").replace("'","''").trim();}public String get_C_badge_name(){return C_badge_name;}
    private String  C_UpdateTime; public void set_C_UpdateTime(String C_UpdateTime){this.C_UpdateTime=C_UpdateTime.replace("\n","").replace("\r","").replace("\t","").replace("'","''").trim();}public String get_C_UpdateTime(){return C_UpdateTime;}
    private String  C_nextString; public void set_C_nextString(String C_nextString){this.C_nextString=C_nextString.replace("\n","").replace("\r","").replace("\t","").replace("'","''").trim();}public String get_C_nextString(){return C_nextString;}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ReplyKouBei that = (ReplyKouBei) o;
        return Objects.equals(C_KouBeiID, that.C_KouBeiID)&& Objects.equals(C_replyId,that.C_replyId)&&Objects.equals(C_TargetUserID,that.C_TargetUserID); // Compare relevant fields
    }

    @Override
    public int hashCode() {
        return Objects.hash(C_KouBeiID,C_replyId,C_TargetUserID); // Use relevant fields
    }
}
