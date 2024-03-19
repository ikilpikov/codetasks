import { getTaskForUpdate } from "../Services/services";
import { useMutation } from "@tanstack/react-query";
import { useTaskInputsStore } from "../store";
export const useTaskForUpdate = () => {
  const { setInputs } = useTaskInputsStore();
  return useMutation({
    mutationFn: (id: number) => getTaskForUpdate(id),
    onSuccess: (data) => setInputs(data.data),
    onError: (error) => console.log(error),
  });
};
