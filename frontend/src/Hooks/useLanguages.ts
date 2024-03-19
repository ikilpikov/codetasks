import { getAllLanguages } from "../Services/services";
import { useQuery } from "@tanstack/react-query";

export const useLanguages = () => {
  return useQuery({
    queryKey: ["programming-language"],
    queryFn: () => getAllLanguages(),
  });
};
