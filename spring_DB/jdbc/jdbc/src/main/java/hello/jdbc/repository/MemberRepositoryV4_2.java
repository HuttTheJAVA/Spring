package hello.jdbc.repository;

import hello.jdbc.domain.Member;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.jdbc.support.SQLErrorCodeSQLExceptionTranslator;
import org.springframework.jdbc.support.SQLExceptionTranslator;

import javax.sql.DataSource;
import java.sql.*;
import java.util.NoSuchElementException;

/**
 * SQLExceptionTranslator 추가
 */
@Slf4j
public class MemberRepositoryV4_2 implements MemberRepository{
    private final DataSource dataSource;
    private final SQLExceptionTranslator exTranslator;
    public MemberRepositoryV4_2(DataSource dataSource){
        this.dataSource = dataSource;
        this.exTranslator = new SQLErrorCodeSQLExceptionTranslator(dataSource);
    }
    public Member save(Member member){
        String sql = "insert into member(member_id,money) values(?,?)";

        Connection con = null;
        PreparedStatement pstmt = null;
        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, member.getMemberId());
            pstmt.setInt(2,member.getMoney());
            pstmt.executeUpdate();
            return member;
        } catch (SQLException e) {
            throw exTranslator.translate("save",sql,e);
        }finally {
            close(con,pstmt,null);
        }

    }

    public Member findById(String memberId){
        String sql = "select * from member where member_id=?";
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1,memberId);

            rs = pstmt.executeQuery();
            if(rs.next()){
                Member member = new Member();
                member.setMemberId(rs.getString("member_id"));
                member.setMoney(rs.getInt("money"));
                return member;
            }else{
                throw new NoSuchElementException("member not found member_id="+memberId);
            }

        } catch (SQLException e) {
            throw exTranslator.translate("findById",sql,e);
        }finally {
            close(con,pstmt,rs);
        }
    }

    public void update(String memberId,int money){
        String sql = "update member set money=? where member_id=?";

        Connection con = null;
        PreparedStatement pstmt = null;

        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setInt(1,money);
            pstmt.setString(2,memberId);
            int resultSize = pstmt.executeUpdate();
            log.info("resultSize={}", resultSize);
        } catch (SQLException e) {
            throw exTranslator.translate("update",sql,e);
        }finally {
            close(con,pstmt,null);
        }

    }

    public void delete(String memberId){
        String sql = "delete from member where member_id=?";
        Connection con = null;
        PreparedStatement pstmt = null;

        try{
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1,memberId);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            throw exTranslator.translate("delete",sql,e);
        }finally {
            close(con,pstmt,null);
        }
    }
    private void close(Connection con, Statement stmt, ResultSet rs){
        JdbcUtils.closeResultSet(rs);
        JdbcUtils.closeStatement(stmt);
        // 주의! 커넥션을 JdbcUtils로 닫으면 안됨. 트랜잭션 도중인 커넥션이라면 유지해야함. 유지 판단 여부는 DataSourceUtils 쪽에서 트랜잭션 동기화 매니저를 참고하여 판단 함(가져다 쓴 커넥션이면 유지, 여기서 만든 커넥션이면 유지 X).
        DataSourceUtils.releaseConnection(con,dataSource);
    }
    private Connection getConnection() throws SQLException {
        // DataSourceUtils를 통해 커넥션을 얻으면 트랜잭션 고유 식별자로 트랜잭션 동기화 매니저에 저장된 커넥션을 얻어다 주기 때문에 트랜잭션이 유지된 커넥션이 반환 됨.
        Connection con = DataSourceUtils.getConnection(dataSource);
        log.info("get connection={}, class={}", con, con.getClass());
        return con;
    }



}
