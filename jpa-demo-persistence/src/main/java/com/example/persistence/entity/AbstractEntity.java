package com.example.persistence.entity;

import com.querydsl.core.types.Path;
import com.querydsl.jpa.impl.JPAUpdateClause;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.beans.Introspector;
import java.lang.reflect.Field;
import java.time.LocalDateTime;

@MappedSuperclass
@Data
public abstract class AbstractEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull
	@Column(name = "record_reg_id", nullable = false)
	private Long recordRegId;

	@Column(name = "record_reg_date", nullable = false, insertable = false)
	private LocalDateTime recordRegDate;

	@NotNull
	@Column(name = "record_upd_id", nullable = false)
	private Long recordUpdId;

	@Column(name = "record_upd_date", nullable = false, insertable = false)
	private LocalDateTime recordUpdDate;

	public void mapForUpdate(JPAUpdateClause jpaUpdateClause) {
		try {
			Class<?> eClass = getClass();
			Field[] efields = eClass.getDeclaredFields();
			Field[] sfields = eClass.getSuperclass().getDeclaredFields();

			String eClassName = eClass.getSimpleName();
			Class<?> qClass = Class.forName(eClass.getPackageName() + ".Q" + eClassName);
			Object qInstance = qClass.getDeclaredField(Introspector.decapitalize(eClassName)).get(qClass);

			for (Field field : efields) {
				field.setAccessible(true);
				Object fieldValue = field.get(this);
				if (fieldValue != null) {
					Path path = (Path<?>) qClass.getDeclaredField(field.getName()).get(qInstance);
					jpaUpdateClause.set(path, fieldValue);
				}
			}
			for (Field field : sfields) {
				field.setAccessible(true);
				Object fieldValue = field.get(this);
				if (fieldValue != null) {
					Path path = (Path<?>) qClass.getDeclaredField(field.getName()).get(qInstance);
					jpaUpdateClause.set(path, fieldValue);
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
}
