import { useMutation } from "@tanstack/react-query";
import { addTask } from "../Services/services";
import { ITaskData } from "../Services/services";
const useAddTask = () => {
  return useMutation({
    mutationFn: (data: ITaskData) => addTask(data),
    onSuccess: (data) => console.log(data),
    onError: (error) => console.log(error),
  });
};
export default useAddTask;
