import {
  Accordion,
  AccordionContent,
  AccordionItem,
  AccordionTrigger,
} from './ui/accordion';
import { ScrollArea } from './ui/scroll-area';

const PlanList = ({ data = [] }: { data: Array<{}> }) => {
  return (
    <ScrollArea className="mt-4">
      <Accordion
        type="single"
        collapsible
        className="flex flex-1 flex-col gap-3 pb-2"
      >
        {!data?.length ? (
          <div className="text-center text-xl font-bold my-5 text-black dark:text-white">
            No Results...
          </div>
        ) : (
          data.map((e: any, i) => (
            <AccordionItem
              key={e?.id}
              value={i + '_list'}
              className="self-center w-[100%]"
            >
              <AccordionTrigger className="flex bg-gray-100 dark:bg-neutral-800 p-5 rounded-sm text-black dark:text-white">
                <div className="flex flex-row flex-grow gap-2 mr-5">
                  <div className="flex flex-1 flex-col gap-2">
                    <span>
                      <span className="font-bold text-xl capitalize mr-2">
                        {e?.Provider}
                      </span>
                      <span className="text-base capitalize">
                        {e?.['Plan Name']}
                      </span>
                    </span>
                    <span>{e?.Capacity}</span>
                  </div>
                  <div className="flex flex-col gap-2">
                    <span className="text-right">{`$${e?.['Price per month'] || '0'} CAD / month`}</span>
                    <span className="text-right">{`$${e?.['Price per annum'] || '0'} CAD / year`}</span>
                  </div>
                </div>
              </AccordionTrigger>
              <AccordionContent className="bg-gray-200 dark:bg-neutral-700 p-5 rounded-b-sm text-black dark:text-white">
                <span>{e?.paymentMethod}</span>
                <span>
                  <ul>
                    {(
                      (e?.['Special features'] || '')
                        .replaceAll('/n')
                        .split('- ') || []
                    )
                      .filter((feat: string) => !!feat)
                      .map((feat: string, index: number) => (
                        <li key={index} className="flex gap-2">
                          <span>-</span>
                          {feat}
                        </li>
                      ))}
                  </ul>
                </span>
              </AccordionContent>
            </AccordionItem>
          ))
        )}
      </Accordion>
    </ScrollArea>
  );
};

export default PlanList;
