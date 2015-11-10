package cn.edu.shnu.fb.interfaces.assembler;

import java.util.ArrayList;
import java.util.List;

import cn.edu.shnu.fb.domain.Imp.Imp;
import cn.edu.shnu.fb.domain.common.Locator;
import cn.edu.shnu.fb.infrastructure.persistence.LocatorDao;
import cn.edu.shnu.fb.interfaces.dto.GridEntityDTO;

/**
 * Created by bytenoob on 15/11/7.
 */
public class ImpAssembler {
    public final static List<GridEntityDTO> toGridEntityDTO(Iterable<Imp> imps){
        if(imps!=null) {
            List<GridEntityDTO> resList = new ArrayList<>();
            for (Imp i : imps) {
                resList.add(new GridEntityDTO(i));
            }
            return resList;
        }else{
            return null;
        }
    }

    public final static Iterable<Imp> differImpListWithGridEntityListByLocator(List<Imp> imps,List<GridEntityDTO> entities){
        List<Imp> resList = new ArrayList<>();
        boolean isDifferent = true;
        for(Imp cImp :imps){
            for(GridEntityDTO entity : entities) {
                if(cImp.getCourse().getId() == entity.getCourseId()) {
                    isDifferent = false;
                    break;
                }
            }
            if(isDifferent){
                resList.add(cImp);
            }
            isDifferent = true;
        }
        return resList;
    }
}
