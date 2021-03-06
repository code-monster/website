package ua.com.itinterview.service;

import org.easymock.Capture;
import org.easymock.EasyMock;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ua.com.itinterview.dao.InterviewDao;
import ua.com.itinterview.dao.UserDao;
import ua.com.itinterview.dao.paging.PagingFilter;
import ua.com.itinterview.entity.*;
import ua.com.itinterview.web.command.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.assertEquals;

public class InterviewServiceUnitTest {
    public static final String FEEDBACK_TEXT = "Feedback text";
    public static final Date CREATED_DATE = new Date(10000);
    public static final int CITY_ID = 1;
    public static final int COMPANY_ID = 2;
    public static final int POSITION_ID = 3;
    public static final int USER_ID = 4;
    public static final int TECHNOLOGY_ID = 5;
    public static final int INTERVIEW_ID = 6;
    private UserEntity user;
    private InterviewService interviewService;
    private InterviewDao interviewDaoMock;
    private UserDao userDao;

    @Before
    public void createMocks() {
        interviewDaoMock = EasyMock.createMock(InterviewDao.class);
        interviewService = new InterviewService();
        interviewService.interviewEntityDao = interviewDaoMock;
        userDao = EasyMock.createMock(UserDao.class);
        interviewService.userDao = userDao;
    }

    @After
    public void verifyAll() {
        verify(interviewDaoMock);
    }

    public void replayAllMocks() {
        replay(interviewDaoMock);
    }

    @Test
    public void testAddInterview() {
        InterviewCommand interviewCommand = createInterviewCommand();
        InterviewEntity interviewEntity = new InterviewEntity(interviewCommand);
        // EasyMock.expect(
        // interviewDaoMock
        // .getInterviewsByUser(interviewCommand.getUser()).size())
        // .andReturn(1);
        EasyMock.expect(interviewDaoMock.save(interviewEntity)).andReturn(
                interviewEntity);
        replayAllMocks();
        interviewService.addInterview(interviewCommand);
        verifyAll();
    }

    @Test
    public void testGetInterviewList() {
        List<InterviewEntity> list = new ArrayList<InterviewEntity>();
        InterviewEntity interviewEntity = createInterviewEntity();
        System.out.println(interviewEntity.getUser());
        list.add(interviewEntity);
        EasyMock.expect(interviewDaoMock.getAll()).andReturn(list);
        EasyMock.replay(interviewDaoMock);
        interviewService.getInterviewList();
        verify(interviewDaoMock);
    }

    @Test
    public void testGetUserInterviewList() {
        List<InterviewEntity> interviewEntities = getInterviewEntities();
        Capture<UserEntity> userEntityCapture = new Capture<UserEntity>();
        Capture<PagingFilter> pagingFilterCapture = new Capture<PagingFilter>();
        expect(interviewDaoMock.getInterviewsByUser(capture(userEntityCapture), capture(pagingFilterCapture))).andReturn(interviewEntities);
        replayAllMocks();
        UserCommand userCommand = createUserCommand();
        List<InterviewCommand> actualInterviewCommands = interviewService.getUserInterviewList(userCommand);
        List<InterviewCommand> expectedInterviewCommands = getInterviewCommands();
        assertEquals(expectedInterviewCommands, actualInterviewCommands);
        UserEntity actualUserEntity = userEntityCapture.getValue();
        UserEntity expectedUserEntity = createUserEntity();
        assertEquals(expectedUserEntity, actualUserEntity);
        assertEquals(expectedUserEntity.getId(), actualUserEntity.getId());
    }

    @Test
    public void testGetUserInterviewListWithUserId() {
        List<InterviewEntity> interviewEntities = getInterviewEntities();
        Capture<UserEntity> userEntityCapture = new Capture<UserEntity>();
        Capture<PagingFilter> pagingFilterCapture = new Capture<PagingFilter>();
        expect(interviewDaoMock.getInterviewsByUser(capture(userEntityCapture), capture(pagingFilterCapture))).andReturn(interviewEntities);
        replayAllMocks();
        UserCommand userCommand = createUserCommand();
        List<InterviewCommand> actualInterviewCommands = interviewService.getUserInterviewList(userCommand.getId());
        List<InterviewCommand> expectedInterviewCommands = getInterviewCommands();
        assertEquals(expectedInterviewCommands, actualInterviewCommands);
        UserEntity actualUserEntity = userEntityCapture.getValue();
        UserEntity expectedUserEntity = createUserEntity();
        assertEquals(expectedUserEntity, actualUserEntity);
        assertEquals(expectedUserEntity.getId(), actualUserEntity.getId());
    }

    private List<InterviewCommand> getInterviewCommands() {
        List<InterviewCommand> expectedInterviewCommands = new ArrayList<InterviewCommand>();
        expectedInterviewCommands.add(createInterviewCommand());
        return expectedInterviewCommands;
    }

    private List<InterviewEntity> getInterviewEntities() {
        List<InterviewEntity> interviewEntities = new ArrayList<InterviewEntity>();
        interviewEntities.add(createInterviewEntity());
        return interviewEntities;
    }

    @Test
    public void testConvertCommandToEntity() {
        replayAllMocks();
        InterviewCommand command = createInterviewCommand();
        InterviewEntity expectedEntity = createInterviewEntity();
        InterviewEntity actualEntity = new InterviewEntity(command);
        assertEquals(expectedEntity, actualEntity);
    }

    @Test
    public void testConvertEntityToCommand() {
        replayAllMocks();
        InterviewEntity interviewEntity = createInterviewEntity();
        InterviewCommand expectedCommand = createInterviewCommand();
        InterviewCommand actualCommand = new InterviewCommand(interviewEntity);
        assertEquals(expectedCommand, actualCommand);
    }

    private InterviewCommand createInterviewCommand() {
        InterviewCommand interviewCommand = new InterviewCommand();
        interviewCommand.setId(INTERVIEW_ID);
        interviewCommand.setFeedback(FEEDBACK_TEXT);
        interviewCommand.setCreated(CREATED_DATE);
        interviewCommand.setCity(createCityCommand());
        interviewCommand.setCompany(createCompanyCommand());
        interviewCommand.setPosition(createPositionCommand());
        interviewCommand.setTechnology(createTechnologyCommand());
        interviewCommand.setUser(createUserCommand());
        return interviewCommand;
    }

    private InterviewEntity createInterviewEntity() {
        InterviewEntity interviewEntity = new InterviewEntity();
        interviewEntity.setId(INTERVIEW_ID);
        interviewEntity.setFeedback(FEEDBACK_TEXT);
        interviewEntity.setCreated(CREATED_DATE);
        interviewEntity.setCity(createCityEntity());
        interviewEntity.setCompany(createCompanyEntity());
        interviewEntity.setPosition(createPositionEntity());
        interviewEntity.setTechnology(createTechnologyEntity());
        interviewEntity.setUser(createUserEntity());
        return interviewEntity;
    }

    public CityCommand createCityCommand() {
        CityCommand cityCommand = new CityCommand();
        cityCommand.setId(CITY_ID);
        return cityCommand;
    }

    public CompanyCommand createCompanyCommand() {
        CompanyCommand companyCommand = new CompanyCommand();
        companyCommand.setId(COMPANY_ID);
        return companyCommand;
    }

    public PositionCommand createPositionCommand() {
        PositionCommand positionCommand = new PositionCommand();
        positionCommand.setId(POSITION_ID);
        return positionCommand;
    }

    public UserCommand createUserCommand() {
        UserCommand userCommand = new UserCommand();
        userCommand.setId(USER_ID);
        return userCommand;
    }

    public TechnologyCommand createTechnologyCommand() {
        TechnologyCommand technologyCommand = new TechnologyCommand();
        technologyCommand.setId(TECHNOLOGY_ID);
        return technologyCommand;
    }

    public CityEntity createCityEntity() {
        CityEntity cityEntity = new CityEntity();
        cityEntity.setId(CITY_ID);
        return cityEntity;
    }

    public CompanyEntity createCompanyEntity() {
        CompanyEntity companyEntity = new CompanyEntity();
        companyEntity.setId(COMPANY_ID);
        return companyEntity;
    }

    public PositionEntity createPositionEntity() {
        PositionEntity positionEntity = new PositionEntity();
        positionEntity.setId(POSITION_ID);
        return positionEntity;
    }

    public UserEntity createUserEntity() {
        UserEntity userEntity = new UserEntity();
        userEntity.setId(USER_ID);
        return userEntity;
    }

    public TechnologyEntity createTechnologyEntity() {
        TechnologyEntity technologyEntity = new TechnologyEntity();
        technologyEntity.setId(TECHNOLOGY_ID);
        return technologyEntity;
    }
}
