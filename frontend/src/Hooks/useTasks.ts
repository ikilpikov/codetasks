import { getAllTasks } from "../Services/services";
import { useQuery } from "@tanstack/react-query";
import { ITasks } from "../Services/services";
import { createRequest } from "../Utils/utils";
import { useTasksStore } from "../store";

export const useTasks = () => {
  const { difficulties, topics, languages, size } = useTasksStore();
  const request: ITasks = { size: size };

  request.difficulties = createRequest(difficulties, "difficulties");
  request.topics = createRequest(topics, "topics");
  request.languages = createRequest(languages, "languages");

  return useQuery({
    queryKey: ["tasks", difficulties, topics, languages, size],
    queryFn: () => getAllTasks(request),
    refetchOnMount: true,
  });
};
