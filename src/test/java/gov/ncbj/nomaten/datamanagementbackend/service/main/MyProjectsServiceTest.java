package gov.ncbj.nomaten.datamanagementbackend.service.main;

import gov.ncbj.nomaten.datamanagementbackend.model.Project;
import gov.ncbj.nomaten.datamanagementbackend.model.User;
import gov.ncbj.nomaten.datamanagementbackend.model.info.Info;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MyProjectsServiceTest {

    MyProjectsService myProjectsService = new MyProjectsService();

    Project project1 = Project.builder().id(1L).build();
    Project project2 = Project.builder().id(2L).build();

    User userA = User.builder().id(1L).username("userA").build();
    User userB = User.builder().id(2L).username("userB").build();
    User userC = User.builder().id(3L).username("userC").build();

    Info info1 = Info.builder().id(1L).infoName("info1").build();
    Info info2 = Info.builder().id(2L).infoName("info").build();
    Info info3 = Info.builder().id(3L).infoName("info").build();
    Info info4 = Info.builder().id(4L).infoName("info4").build();
    Info info5 = Info.builder().id(5L).infoName("info5").build();

    @BeforeEach
    void init() {
        List<Info> userAInfos = new LinkedList<>();
        userAInfos.add(info1);
        userAInfos.add(info2);
        info1.setUser(userA);
        info2.setUser(userA);
        userA.setInfoList(userAInfos);

        List<Info> userBInfos = new LinkedList<>();
        userBInfos.add(info3);
        userBInfos.add(info4);
        info3.setUser(userB);
        info4.setUser(userB);
        userB.setInfoList(userBInfos);

        List<Info> userCInfos = new LinkedList<>();
        userCInfos.add(info5);
        info5.setUser(userC);
        userC.setInfoList(userCInfos);

        List<User> project1Users = new LinkedList<>();
        project1Users.add(userA);
        project1Users.add(userB);
        project1.setUsers(project1Users);
        List<Info> project1Infos = new LinkedList<>();
        project1Infos.add(info1);
        project1Infos.add(info3);
        project1.setInfoList(project1Infos);

        List<User> project2Users = new LinkedList<>();
        project2Users.add(userB);
        project2Users.add(userC);
        project2.setUsers(project2Users);
        List<Info> project2Infos = new LinkedList<>();
        project2Infos.add(info4);
        project2Infos.add(info5);
        project2.setInfoList(project2Infos);

    }

    @Nested
    class IsUserInTheProject {
        @Test
        void isUserInTheProject() {
            assertTrue(myProjectsService.isUserInTheProject(userA, project1));
            assertTrue(myProjectsService.isUserInTheProject(userB, project1));
            assertTrue(myProjectsService.isUserInTheProject(userB, project2));
            assertTrue(myProjectsService.isUserInTheProject(userC, project2));
            assertFalse(myProjectsService.isUserInTheProject(userC, project1));
            assertFalse(myProjectsService.isUserInTheProject(userA, project2));
        }
    }

    @Nested
    class IsInfoOfUserInTheProject {
        @Test
        void isInfoOfUserInTheProject() {
            assertTrue(myProjectsService.isInfoOfUserInTheProject(userA, info1, project1));
            assertFalse(myProjectsService.isInfoOfUserInTheProject(userA, info2, project1));
            assertTrue(myProjectsService.isInfoOfUserInTheProject(userB, info3, project1));
            assertThrows(RuntimeException.class,
                    () -> myProjectsService.isInfoOfUserInTheProject(userA, info3, project1),
                    "Info and user do not match"); // true even if message different
            assertThrows(RuntimeException.class,
                    () -> myProjectsService.isInfoOfUserInTheProject(userC, info3, project1),
                    "User is not in the project");
            assertTrue(myProjectsService.isInfoOfUserInTheProject(userB, info4, project2));
            assertThrows(RuntimeException.class,
                    () -> myProjectsService.isInfoOfUserInTheProject(userA, info5, project2),
                    "User is not in the project");
        }
    }

}