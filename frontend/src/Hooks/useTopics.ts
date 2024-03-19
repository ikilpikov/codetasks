import { getAllTopics } from "../Services/services";
import { useQuery } from "@tanstack/react-query";

export const useTopics = () => {
  return useQuery({
    queryKey: ["topic"],
    queryFn: () => getAllTopics(),
  });
};
