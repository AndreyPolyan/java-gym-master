package ru.yandex.practicum.gym;

import java.util.*;

public class Timetable {
    // Пытаемся усидеть на двух стульях
    // Тк нужно O(1) для дня, но все тренировки - коллекция -> сортированный список sessionsByDay
    // Тк нужно O(logN) для дня/времени и поддержание сортировки по времени - TreeMap в sessionsByDayAndTime
    private final Map<DayOfWeek, List<TrainingSession>> sessionsByDay;
    private final Map<DayOfWeek, TreeMap<TimeOfDay, List<TrainingSession>>> sessionsByDayAndTime;
    private final Map<Coach, Integer> trainingCountByCoach;

    public Timetable() {
        sessionsByDay = new EnumMap<>(DayOfWeek.class);
        sessionsByDayAndTime = new EnumMap<>(DayOfWeek.class);
        trainingCountByCoach = new HashMap<>();

        for (DayOfWeek dayOfWeek : DayOfWeek.values()) {
            sessionsByDay.put(dayOfWeek, new ArrayList<>());
            sessionsByDayAndTime.put(dayOfWeek, new TreeMap<>());
        }
    }

    public void addNewTrainingSession(TrainingSession trainingSession) {
        DayOfWeek dayOfWeek = trainingSession.getDayOfWeek();
        TimeOfDay timeOfDay = trainingSession.getTimeOfDay();
        Coach coach = trainingSession.getCoach();

        if (hasCoachConflict(dayOfWeek, timeOfDay, coach)) {
            System.out.printf("Невозможно добавить тренировку: у тренера %s уже есть занятие в этот день и время.%n", coach);
            return;
        }

        List<TrainingSession> sessionsForDay = sessionsByDay.get(dayOfWeek);
        int insertIndex = findInsertIndex(sessionsForDay, timeOfDay);
        sessionsForDay.add(insertIndex, trainingSession);

        TreeMap<TimeOfDay, List<TrainingSession>> sessionsForDayAndTime = sessionsByDayAndTime.get(dayOfWeek);
        sessionsForDayAndTime
                .computeIfAbsent(timeOfDay, key -> new ArrayList<>())
                .add(trainingSession);
        trainingCountByCoach.merge(coach, 1, Integer::sum);
    }

    public List<TrainingSession> getTrainingSessionsForDay(DayOfWeek dayOfWeek) {
        return Collections.unmodifiableList(sessionsByDay.get(dayOfWeek));
    }

    public List<TrainingSession> getTrainingSessionsForDayAndTime(DayOfWeek dayOfWeek, int hours, int minutes) {
        final TimeOfDay timeOfDay = new TimeOfDay(hours, minutes);
        return getTrainingSessionsForDayAndTime(dayOfWeek, timeOfDay);
    }

    public List<TrainingSession> getTrainingSessionsForDayAndTime(DayOfWeek dayOfWeek, TimeOfDay timeOfDay) {
        List<TrainingSession> sessions =
                sessionsByDayAndTime.get(dayOfWeek).getOrDefault(timeOfDay, Collections.emptyList());

        return Collections.unmodifiableList(sessions);
    }

    public Map<Coach, Integer> getCountByCoaches() {
        List<Map.Entry<Coach, Integer>> entries = new ArrayList<>(trainingCountByCoach.entrySet());

        entries.sort((entry1, entry2) -> {
            int countCompare = Integer.compare(entry2.getValue(), entry1.getValue());
            if (countCompare != 0) {
                return countCompare;
            }

            return entry1.getKey().compareTo(entry2.getKey());
        });

        Map<Coach, Integer> sortedCountByCoach = new LinkedHashMap<>();
        for (Map.Entry<Coach, Integer> entry : entries) {
            sortedCountByCoach.put(entry.getKey(), entry.getValue());
        }

        return Collections.unmodifiableMap(sortedCountByCoach);
    }

    private int findInsertIndex(List<TrainingSession> sessions, TimeOfDay timeOfDay) {
        int left = 0;
        int right = sessions.size();

        while (left < right) {
            int middle = (left + right) / 2;
            TimeOfDay middleTime = sessions.get(middle).getTimeOfDay();

            if (middleTime.compareTo(timeOfDay) <= 0) {
                left = middle + 1;
            } else {
                right = middle;
            }
        }

        return left;
    }
    
    private boolean hasCoachConflict(DayOfWeek dayOfWeek, TimeOfDay timeOfDay, Coach coach) {
        List<TrainingSession> sessionsInTimeSlot =
                sessionsByDayAndTime.get(dayOfWeek).getOrDefault(timeOfDay, Collections.emptyList());

        for (TrainingSession trainingSession : sessionsInTimeSlot) {
            if (trainingSession.getCoach().equals(coach)) {
                return true;
            }
        }

        return false;
    }
}
