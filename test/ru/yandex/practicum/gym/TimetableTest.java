package ru.yandex.practicum.gym;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class TimetableTest {

    @Test
    void testGetTrainingSessionsForDaySingleSession() {
        Timetable timetable = new Timetable();

        Group group = new Group("Акробатика для детей", Age.CHILD, 60);
        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");
        TrainingSession singleTrainingSession = new TrainingSession(
                group,
                coach,
                DayOfWeek.MONDAY,
                new TimeOfDay(13, 0)
        );

        timetable.addNewTrainingSession(singleTrainingSession);

        List<TrainingSession> mondaySessions = timetable.getTrainingSessionsForDay(DayOfWeek.MONDAY);
        List<TrainingSession> tuesdaySessions = timetable.getTrainingSessionsForDay(DayOfWeek.TUESDAY);

        Assertions.assertEquals(1, mondaySessions.size(), "За понедельник должно вернуться одно занятие.");
        // Мы не переопределяли equals, но для данного теста сравнения адресов будет достаточно
        Assertions.assertEquals(singleTrainingSession, mondaySessions.get(0), "Вернулась неверная тренировка.");
        Assertions.assertTrue(tuesdaySessions.isEmpty(), "За вторник не должно вернуться занятий.");
    }

    @Test
    void testGetTrainingSessionsForDayMultipleSessions() {
        Timetable timetable = new Timetable();

        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");

        Group groupAdult = new Group("Акробатика для взрослых", Age.ADULT, 90);
        TrainingSession thursdayAdultTrainingSession = new TrainingSession(
                groupAdult,
                coach,
                DayOfWeek.THURSDAY,
                new TimeOfDay(20, 0)
        );

        timetable.addNewTrainingSession(thursdayAdultTrainingSession);

        Group groupChild = new Group("Акробатика для детей", Age.CHILD, 60);
        TrainingSession mondayChildTrainingSession = new TrainingSession(
                groupChild,
                coach,
                DayOfWeek.MONDAY,
                new TimeOfDay(13, 0)
        );
        TrainingSession thursdayChildTrainingSession = new TrainingSession(
                groupChild,
                coach,
                DayOfWeek.THURSDAY,
                new TimeOfDay(13, 0)
        );
        TrainingSession saturdayChildTrainingSession = new TrainingSession(
                groupChild,
                coach,
                DayOfWeek.SATURDAY,
                new TimeOfDay(10, 0)
        );

        timetable.addNewTrainingSession(mondayChildTrainingSession);
        timetable.addNewTrainingSession(thursdayChildTrainingSession);
        timetable.addNewTrainingSession(saturdayChildTrainingSession);

        List<TrainingSession> mondaySessions = timetable.getTrainingSessionsForDay(DayOfWeek.MONDAY);
        List<TrainingSession> thursdaySessions = timetable.getTrainingSessionsForDay(DayOfWeek.THURSDAY);
        List<TrainingSession> tuesdaySessions = timetable.getTrainingSessionsForDay(DayOfWeek.TUESDAY);

        Assertions.assertEquals(1, mondaySessions.size(), "За понедельник должно вернуться одно занятие.");
        Assertions.assertEquals(mondayChildTrainingSession, mondaySessions.get(0), "За понедельник вернулась неверная тренировка.");

        Assertions.assertEquals(2, thursdaySessions.size(), "За четверг должно вернуться два занятия.");
        Assertions.assertEquals(thursdayChildTrainingSession, thursdaySessions.get(0), "Первой должна идти тренировка в 13:00.");
        Assertions.assertEquals(thursdayAdultTrainingSession, thursdaySessions.get(1), "Второй должна идти тренировка в 20:00.");

        Assertions.assertTrue(tuesdaySessions.isEmpty(), "За вторник не должно вернуться занятий.");
    }

    @Test
    void testGetTrainingSessionsForDayAndTime() {
        Timetable timetable = new Timetable();

        Group group = new Group("Акробатика для детей", Age.CHILD, 60);
        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");
        TrainingSession singleTrainingSession = new TrainingSession(
                group,
                coach,
                DayOfWeek.MONDAY,
                new TimeOfDay(13, 0)
        );

        timetable.addNewTrainingSession(singleTrainingSession);

        List<TrainingSession> mondayAtThirteen = timetable.getTrainingSessionsForDayAndTime(
                DayOfWeek.MONDAY,
                new TimeOfDay(13, 0)
        );
        List<TrainingSession> mondayAtFourteen = timetable.getTrainingSessionsForDayAndTime(
                DayOfWeek.MONDAY,
                new TimeOfDay(14, 0)
        );

        Assertions.assertEquals(1, mondayAtThirteen.size(), "За понедельник в 13:00 должно вернуться одно занятие.");
        Assertions.assertEquals(singleTrainingSession, mondayAtThirteen.get(0), "Вернулась неверная тренировка.");
        Assertions.assertTrue(mondayAtFourteen.isEmpty(), "За понедельник в 14:00 занятий быть не должно.");
    }

    @Test
    void testGetCountByCoachesSingleCoach() {
        Timetable timetable = new Timetable();

        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");
        Group group1 = new Group("Акробатика для детей", Age.CHILD, 60);
        Group group2 = new Group("Растяжка для взрослых", Age.ADULT, 90);

        timetable.addNewTrainingSession(new TrainingSession(group1, coach, DayOfWeek.MONDAY, new TimeOfDay(10, 0)));
        timetable.addNewTrainingSession(new TrainingSession(group2, coach, DayOfWeek.WEDNESDAY, new TimeOfDay(18, 0)));

        Map<Coach, Integer> countByCoaches = timetable.getCountByCoaches();

        Assertions.assertEquals(1, countByCoaches.size(), "В статистике должен быть один тренер.");
        Assertions.assertEquals(2, countByCoaches.get(coach), "У тренера должно быть две тренировки.");
    }

    @Test
    void testGetCountByCoachesSortedByCountDescending() {
        Timetable timetable = new Timetable();

        Coach coach1 = new Coach("Васильев", "Николай", "Сергеевич");
        Coach coach2 = new Coach("Иванов", "Петр", "Александрович");
        Group group = new Group("Акробатика", Age.CHILD, 60);

        timetable.addNewTrainingSession(new TrainingSession(group, coach1, DayOfWeek.MONDAY, new TimeOfDay(10, 0)));
        timetable.addNewTrainingSession(new TrainingSession(group, coach1, DayOfWeek.TUESDAY, new TimeOfDay(10, 0)));
        timetable.addNewTrainingSession(new TrainingSession(group, coach1, DayOfWeek.WEDNESDAY, new TimeOfDay(10, 0)));

        timetable.addNewTrainingSession(new TrainingSession(group, coach2, DayOfWeek.THURSDAY, new TimeOfDay(10, 0)));

        Map<Coach, Integer> countByCoaches = timetable.getCountByCoaches();
        Iterator<Map.Entry<Coach, Integer>> iterator = countByCoaches.entrySet().iterator();

        Map.Entry<Coach, Integer> first = iterator.next();
        Map.Entry<Coach, Integer> second = iterator.next();

        Assertions.assertEquals(coach1, first.getKey(), "Первым должен идти тренер с большим числом тренировок.");
        Assertions.assertEquals(3, first.getValue(), "У первого тренера должно быть три тренировки.");

        Assertions.assertEquals(coach2, second.getKey(), "Вторым должен идти тренер с меньшим числом тренировок.");
        Assertions.assertEquals(1, second.getValue(), "У второго тренера должна быть одна тренировка.");
    }

    @Test
    void testGetCountByCoachesWithEqualCountsSortedByCoachName() {
        Timetable timetable = new Timetable();

        Coach coach1 = new Coach("Иванов", "Петр", "Александрович");
        Coach coach2 = new Coach("Сидоров", "Алексей", "Игоревич");
        Group group = new Group("Акробатика", Age.CHILD, 60);

        timetable.addNewTrainingSession(new TrainingSession(group, coach2, DayOfWeek.MONDAY, new TimeOfDay(10, 0)));
        timetable.addNewTrainingSession(new TrainingSession(group, coach1, DayOfWeek.TUESDAY, new TimeOfDay(10, 0)));

        Map<Coach, Integer> countByCoaches = timetable.getCountByCoaches();
        Iterator<Map.Entry<Coach, Integer>> iterator = countByCoaches.entrySet().iterator();

        Map.Entry<Coach, Integer> first = iterator.next();
        Map.Entry<Coach, Integer> second = iterator.next();

        Assertions.assertEquals(coach1, first.getKey(), "При равном числе тренировок тренеры должны сортироваться по ФИО.");
        Assertions.assertEquals(coach2, second.getKey(), "Вторым должен идти тренер, который позже по ФИО.");
    }

    @Test
    void testCoachConflictInSameDayAndTime() {
        Timetable timetable = new Timetable();

        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");
        Group group1 = new Group("Акробатика для детей", Age.CHILD, 60);
        Group group2 = new Group("Растяжка для взрослых", Age.ADULT, 90);

        TrainingSession firstSession = new TrainingSession(group1, coach, DayOfWeek.MONDAY, new TimeOfDay(13, 0));
        TrainingSession conflictingSession = new TrainingSession(group2, coach, DayOfWeek.MONDAY, new TimeOfDay(13, 0));

        timetable.addNewTrainingSession(firstSession);
        timetable.addNewTrainingSession(conflictingSession);

        List<TrainingSession> mondayAtThirteen = timetable.getTrainingSessionsForDayAndTime(
                DayOfWeek.MONDAY,
                new TimeOfDay(13, 0)
        );
        Map<Coach, Integer> countByCoaches = timetable.getCountByCoaches();

        Assertions.assertEquals(1, mondayAtThirteen.size(), "Конфликтующая тренировка не должна быть добавлена.");
        Assertions.assertEquals(firstSession, mondayAtThirteen.get(0), "В расписании должна остаться только первая тренировка.");
        Assertions.assertEquals(1, countByCoaches.get(coach), "Счетчик тренера не должен увеличиться из-за конфликтующей тренировки.");
    }

    @Test
    void testSessionsAreSortedAfterRandomInsertions() {
        Timetable timetable = new Timetable();

        Coach coach1 = new Coach("Васильев", "Николай", "Сергеевич");
        Coach coach2 = new Coach("Иванов", "Петр", "Александрович");
        Coach coach3 = new Coach("Сидоров", "Алексей", "Игоревич");
        Coach coach4 = new Coach("Кузнецов", "Роман", "Олегович");

        Group group = new Group("Акробатика", Age.CHILD, 60);

        TrainingSession session18 = new TrainingSession(group, coach1, DayOfWeek.MONDAY, new TimeOfDay(18, 0));
        TrainingSession session10 = new TrainingSession(group, coach2, DayOfWeek.MONDAY, new TimeOfDay(10, 0));
        TrainingSession session15 = new TrainingSession(group, coach3, DayOfWeek.MONDAY, new TimeOfDay(15, 0));
        TrainingSession session10Second = new TrainingSession(group, coach4, DayOfWeek.MONDAY, new TimeOfDay(10, 0));

        timetable.addNewTrainingSession(session18);
        timetable.addNewTrainingSession(session10);
        timetable.addNewTrainingSession(session15);
        timetable.addNewTrainingSession(session10Second);

        List<TrainingSession> mondaySessions = timetable.getTrainingSessionsForDay(DayOfWeek.MONDAY);

        Assertions.assertEquals(4, mondaySessions.size(), "Все неконфликтующие тренировки должны быть добавлены.");
        Assertions.assertEquals(session10, mondaySessions.get(0), "Первой должна идти одна из тренировок на 10:00.");
        Assertions.assertEquals(session10Second, mondaySessions.get(1), "Второй должна идти вторая тренировка на 10:00.");
        Assertions.assertEquals(session15, mondaySessions.get(2), "Третьей должна идти тренировка на 15:00.");
        Assertions.assertEquals(session18, mondaySessions.get(3), "Последней должна идти тренировка на 18:00.");

        for (int i = 1; i < mondaySessions.size(); i++) {
            TimeOfDay previousTime = mondaySessions.get(i - 1).getTimeOfDay();
            TimeOfDay currentTime = mondaySessions.get(i).getTimeOfDay();

            Assertions.assertTrue(
                    previousTime.compareTo(currentTime) <= 0,
                    "Список тренировок должен оставаться отсортированным по времени."
            );
        }
    }
}